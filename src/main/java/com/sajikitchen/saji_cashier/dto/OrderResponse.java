package com.sajikitchen.saji_cashier.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderResponse {
    private String orderId;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String createdAt; // Format: dd MMM yyyy HH:mm:ss
    private String paymentConfirmedAt; // <-- Field baru
}
