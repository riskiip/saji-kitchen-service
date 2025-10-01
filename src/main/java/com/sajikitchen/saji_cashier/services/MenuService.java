package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.ProductResponse;
import com.sajikitchen.saji_cashier.dto.ToppingResponse;

import java.util.List;

public interface MenuService {
    List<ProductResponse> getAllProducts();
    List<ToppingResponse> getAllToppings();
}
