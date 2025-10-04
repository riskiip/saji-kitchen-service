package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.CreateOrderRequest;
import com.sajikitchen.saji_cashier.dto.OrderItemRequest;
import com.sajikitchen.saji_cashier.dto.OrderItemResponse;
import com.sajikitchen.saji_cashier.dto.OrderResponse;
import com.sajikitchen.saji_cashier.models.*;
import com.sajikitchen.saji_cashier.repositories.*;
import com.sajikitchen.saji_cashier.services.email.EmailService;
import com.sajikitchen.saji_cashier.services.pdf.PdfGenerationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ToppingRepository toppingRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PdfGenerationService pdfGenerationService;
    private final EmailService emailService;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(request.getCustomerEmail());
                    return customerRepository.save(newCustomer);
                });

        // Ganti dengan logic untuk mengambil user yang sedang login
        User cashier = userRepository.findByUsername("kasir01")
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found"));

        Order newOrder = new Order();
        newOrder.setOrderId(generateOrderId());
        newOrder.setCustomer(customer);
        newOrder.setUser(cashier);
        newOrder.setPaymentStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemReq.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant not found: " + itemReq.getVariantId()));

            BigDecimal itemSubTotal = variant.getPrice();
            Topping topping = null;
            BigDecimal toppingPrice = BigDecimal.ZERO;

            if (itemReq.getToppingId() != null) {
                topping = toppingRepository.findById(itemReq.getToppingId())
                        .orElseThrow(() -> new EntityNotFoundException("Topping not found: " + itemReq.getToppingId()));
                toppingPrice = topping.getPrice();
                itemSubTotal = itemSubTotal.add(toppingPrice);
            }

            totalAmount = totalAmount.add(itemSubTotal.multiply(new BigDecimal(itemReq.getQuantity())));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProductVariant(variant);
            orderItem.setTopping(topping);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPriceAtPurchase(variant.getPrice());
            orderItem.setToppingPriceAtPurchase(toppingPrice);
            orderItems.add(orderItem);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(newOrder);
        return mapOrderToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmPayment(String orderId) {
        Order order = findOrderByIdOrThrow(orderId);

        if (!"PENDING".equals(order.getPaymentStatus())) {
            throw new IllegalStateException("Order is not in PENDING status.");
        }

        order.setPaymentStatus("PAID");
        order.setPaymentConfirmedAt(OffsetDateTime.now(ZoneId.of("Asia/Jakarta")));

        Order updatedOrder = orderRepository.save(order);

        // Menjalankan proses pembuatan PDF dan pengiriman email
        // TODO: Comment sementara generate pdf dan kirim email, uncomment lagi nanti
//        try {
//            log.info("Starting PDF generation for order ID: {}", updatedOrder.getOrderId());
//            byte[] pdfReceipt = pdfGenerationService.generateOrderReceipt(updatedOrder);
//            log.info("PDF generated successfully, size: {} bytes", pdfReceipt.length);
//
//            log.info("Sending order confirmation email to: {}", updatedOrder.getCustomer().getEmail());
//            emailService.sendOrderConfirmationEmail(updatedOrder.getCustomer().getEmail(), updatedOrder, pdfReceipt);
//            log.info("Email sent successfully for order ID: {}", updatedOrder.getOrderId());
//        } catch (Exception e) {
//            // Gunakan logger untuk mencatat error. Ini tidak akan menghentikan response ke user.
//            log.error("Failed to generate PDF or send email for order ID: {}", orderId, e);
//        }

        return mapOrderToResponse(updatedOrder);
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = findOrderByIdOrThrow(orderId);
        return mapOrderToResponse(order);
    }

    private Order findOrderByIdOrThrow(String orderId) {
        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    private String generateOrderId() {
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd").format(OffsetDateTime.now());
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + uuidPart;
    }

    private OrderResponse mapOrderToResponse(Order order) {
        List<OrderItemResponse> itemResponses = (order.getOrderItems() == null) ? new ArrayList<>() :
                order.getOrderItems().stream()
                        .map(this::mapOrderItemToResponse)
                        .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(formatTimestamp(order.getOrderDate()))
                .customerEmail(order.getCustomer().getEmail())
                .cashierUsername(order.getUser().getUsername())
                .items(itemResponses)
                .build();
    }

    private OrderItemResponse mapOrderItemToResponse(OrderItem item) {
        BigDecimal subTotal = item.getPriceAtPurchase()
                .add(item.getToppingPriceAtPurchase() != null ? item.getToppingPriceAtPurchase() : BigDecimal.ZERO)
                .multiply(new BigDecimal(item.getQuantity()));

        return OrderItemResponse.builder()
                .productName(item.getProductVariant().getProduct().getName())
                .variantName(item.getProductVariant().getName())
                .toppingName(item.getTopping() != null ? item.getTopping().getName() : null)
                .quantity(item.getQuantity())
                .price(item.getPriceAtPurchase())
                .toppingPrice(item.getToppingPriceAtPurchase())
                .subTotal(subTotal)
                .build();
    }

    private String formatTimestamp(OffsetDateTime timestamp) {
        if (timestamp == null) return null;
        return timestamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"));
    }
}