package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder // Pastikan ada @Builder
public class ProductVariantResponse {
    private UUID variantId;
    private String variantName; // Ganti nama agar lebih jelas
    private BigDecimal price;

    // Field tambahan dari Product
    private String productName;
    private String imageUrl;
    private String description;
}
