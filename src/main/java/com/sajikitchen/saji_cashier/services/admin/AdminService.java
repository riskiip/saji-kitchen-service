package com.sajikitchen.saji_cashier.services.admin;

import com.sajikitchen.saji_cashier.dto.admin.DailySalesDetailDto;
import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;
import com.sajikitchen.saji_cashier.dto.admin.ProductRequestDto;
import com.sajikitchen.saji_cashier.models.Product;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    DashboardDataDto getDashboardData();
    List<DailySalesDetailDto> getDailySalesDetail(String date);
    Product createProduct(ProductRequestDto request);
    Product updateProduct(UUID productId, ProductRequestDto request);
    void deleteProduct(UUID productId);
}