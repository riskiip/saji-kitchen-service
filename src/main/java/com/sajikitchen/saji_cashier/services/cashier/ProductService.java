package com.sajikitchen.saji_cashier.services.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ProductVariantResponse;

import java.util.List;


public interface ProductService {
    List<ProductVariantResponse> getActiveProductVariants();
}