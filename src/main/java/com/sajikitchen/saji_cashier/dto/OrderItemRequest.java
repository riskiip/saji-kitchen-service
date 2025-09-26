package com.sajikitchen.saji_cashier.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Integer variantId;
    private Integer toppingId; // Boleh null
    private Integer quantity;
}
