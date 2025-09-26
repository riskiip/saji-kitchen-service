package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.ProductVariantResponse;

import java.util.List;


public interface ProductService {
    List<ProductVariantResponse> getActiveProductVariants();
}