package com.sajikitchen.saji_cashier.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private String variantId;
    private String toppingId; // Boleh null
    private Integer quantity;
}
