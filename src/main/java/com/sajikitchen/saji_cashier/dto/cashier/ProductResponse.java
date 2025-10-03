package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID productId;
    private String name;
    private String description;
    private String imageUrl;
    private boolean isActive;
    private List<ProductVariantResponse> variants;
}