package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.CreateOrderRequest;
import com.sajikitchen.saji_cashier.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
}
