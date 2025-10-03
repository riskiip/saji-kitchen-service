package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String customerEmail;
    private List<OrderItemRequest> items;
}
