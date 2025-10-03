package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;
}
