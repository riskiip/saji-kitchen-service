package com.sajikitchen.saji_cashier.services.admin;

import com.sajikitchen.saji_cashier.dto.admin.*;
import com.sajikitchen.saji_cashier.models.Product;
import com.sajikitchen.saji_cashier.models.ProductVariant;
import com.sajikitchen.saji_cashier.models.Topping;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    DashboardDataDto getDashboardData();
    List<DailySalesDetailDto> getDailySalesDetail(String date);

    Product createProduct(ProductRequestDto request);
    Product updateProduct(UUID productId, ProductRequestDto request);
    void deleteProduct(UUID productId);

    // --- Variant CRUD Methods ---
    ProductVariant createVariant(UUID productId, VariantRequestDto request);
    ProductVariant updateVariant(UUID variantId, VariantRequestDto request);

    // --- Topping CRUD Methods ---
    Topping createTopping(ToppingRequestDto request);
    Topping updateTopping(UUID toppingId, ToppingRequestDto request);

    List<UserResponseDto> findAllCashiers();
    UserResponseDto createCashier(CreateUserRequestDto request);
    UserResponseDto updateCashier(UUID userId, UpdateUserRequestDto request);
}