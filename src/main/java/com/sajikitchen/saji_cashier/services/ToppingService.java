package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.ToppingResponse;

import java.util.List;

public interface ToppingService {
    List<ToppingResponse> getActiveToppings();
}
