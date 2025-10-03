package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID variantId;
    private UUID toppingId;
    private Integer quantity;
}
