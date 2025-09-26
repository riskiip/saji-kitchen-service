package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.CreateOrderRequest;
import com.sajikitchen.saji_cashier.dto.OrderItemRequest;
import com.sajikitchen.saji_cashier.dto.OrderResponse;
import com.sajikitchen.saji_cashier.models.*;
import com.sajikitchen.saji_cashier.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    // Inject semua repository yang dibutuhkan
    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ToppingRepository toppingRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository; // Untuk mengambil data kasir

    public OrderServiceImpl(OrderRepository orderRepository, ProductVariantRepository productVariantRepository, ToppingRepository toppingRepository, CustomerRepository customerRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productVariantRepository = productVariantRepository;
        this.toppingRepository = toppingRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Memastikan semua operasi database berhasil atau semua akan di-rollback
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 1. Cari atau buat customer baru
        Customer customer = customerRepository.findByEmail(request.getCustomerEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(request.getCustomerEmail());
                    return customerRepository.save(newCustomer);
                });

        // 2. Ambil data kasir yang sedang login (untuk contoh, kita hardcode user ID 1)
        //    Dalam aplikasi nyata, ini akan diambil dari Spring Security Context
        User cashier = userRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found"));

        Order newOrder = new Order();
        newOrder.setOrderId(generateOrderId());
        newOrder.setCustomer(customer);
        newOrder.setUser(cashier);
        newOrder.setPaymentStatus("PENDING");
        newOrder.setOrderDate(OffsetDateTime.now(ZoneId.of("Asia/Jakarta")));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Loop melalui setiap item dalam request untuk validasi dan kalkulasi harga
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product Variant with ID " + itemRequest.getVariantId() + " not found"));

            BigDecimal itemPrice = variant.getPrice();
            BigDecimal toppingPrice = BigDecimal.ZERO;
            Topping topping = null;

            if (itemRequest.getToppingId() != null) {
                topping = toppingRepository.findById(itemRequest.getToppingId())
                        .orElseThrow(() -> new EntityNotFoundException("Topping with ID " + itemRequest.getToppingId() + " not found"));
                toppingPrice = topping.getPrice();
            }

            // Kalkulasi total harga untuk item ini (termasuk topping) dikali kuantitas
            BigDecimal subTotal = (itemPrice.add(toppingPrice)).multiply(new BigDecimal(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subTotal);

            // Buat entity OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProductVariant(variant);
            orderItem.setTopping(topping);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(itemPrice);
            orderItem.setToppingPriceAtPurchase(toppingPrice);
            orderItems.add(orderItem);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderItems(orderItems);

        // 4. Simpan order ke database
        Order savedOrder = orderRepository.save(newOrder);

        // 5. Buat dan kembalikan response DTO
        return OrderResponse.builder()
                .orderId(savedOrder.getOrderId())
                .totalAmount(savedOrder.getTotalAmount())
                .paymentStatus(savedOrder.getPaymentStatus())
                .createdAt(formatTimestamp(savedOrder.getOrderDate()))
                .build();
    }

    private String generateOrderId() {
        // Format: ORD-YYYYMMDD-UUID(8)
        String datePart = DateTimeFormatter.ofPattern("yyyyMMdd").format(OffsetDateTime.now());
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + uuidPart;
    }

    private String formatTimestamp(OffsetDateTime timestamp) {
        if (timestamp == null) return null;
        // Format: 'D MMM YYYY HH:mm:ss'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        return timestamp.format(formatter);
    }
}
