package com.sajikitchen.saji_cashier.services.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ProductResponse;
import com.sajikitchen.saji_cashier.dto.cashier.ToppingResponse;

import java.util.List;

public interface MenuService {
    List<ProductResponse> getAllProducts();
    List<ToppingResponse> getAllToppings();
}
