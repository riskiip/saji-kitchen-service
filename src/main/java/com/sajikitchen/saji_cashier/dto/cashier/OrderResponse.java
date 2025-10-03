package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String orderId;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String createdAt;
    private String customerEmail;
    private String cashierUsername;
    private List<OrderItemResponse> items;
}
