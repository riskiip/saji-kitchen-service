package com.sajikitchen.saji_cashier.controllers.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.CreateOrderRequest;
import com.sajikitchen.saji_cashier.dto.cashier.ErrorSchema;
import com.sajikitchen.saji_cashier.dto.cashier.OrderResponse;
import com.sajikitchen.saji_cashier.dto.cashier.StandardApiResponse;
import com.sajikitchen.saji_cashier.services.cashier.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<StandardApiResponse<OrderResponse>> createNewOrder(@RequestBody CreateOrderRequest request) {
        // Panggil service untuk memproses pesanan
        OrderResponse orderResponse = orderService.createOrder(request);

        // Siapkan respons standar
        StandardApiResponse<OrderResponse> response = new StandardApiResponse<>();

        ErrorSchema.ErrorMessage message = new ErrorSchema.ErrorMessage("Success, order created.", "Berhasil, pesanan telah dibuat.");
        ErrorSchema errorSchema = new ErrorSchema("SAJI-00-001", message);

        response.setErrorSchema(errorSchema);
        response.setOutputSchema(orderResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/payment-confirmation")
    @PreAuthorize("hasAuthority('CASHIER')")
    public ResponseEntity<StandardApiResponse<OrderResponse>> confirmOrderPayment(@PathVariable String orderId) {
        // Panggil service untuk konfirmasi pembayaran
        OrderResponse orderResponse = orderService.confirmPayment(orderId);

        // Siapkan respons standar
        StandardApiResponse<OrderResponse> response = new StandardApiResponse<>();

        ErrorSchema.ErrorMessage message = new ErrorSchema.ErrorMessage("Success, payment has been confirmed.", "Berhasil, pembayaran telah dikonfirmasi.");
        ErrorSchema errorSchema = new ErrorSchema("SAJI-00-001", message);

        response.setErrorSchema(errorSchema);
        response.setOutputSchema(orderResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
