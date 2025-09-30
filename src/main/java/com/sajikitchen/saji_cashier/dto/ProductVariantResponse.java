package com.sajikitchen.saji_cashier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductVariantResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String productName;
    private String imageUrl; // <-- Tambahan
    private String description; // <-- Tambahan
}
