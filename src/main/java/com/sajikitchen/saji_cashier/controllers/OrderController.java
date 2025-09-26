package com.sajikitchen.saji_cashier.controllers;

import com.sajikitchen.saji_cashier.dto.CreateOrderRequest;
import com.sajikitchen.saji_cashier.dto.ErrorSchema;
import com.sajikitchen.saji_cashier.dto.OrderResponse;
import com.sajikitchen.saji_cashier.dto.StandardApiResponse;
import com.sajikitchen.saji_cashier.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
