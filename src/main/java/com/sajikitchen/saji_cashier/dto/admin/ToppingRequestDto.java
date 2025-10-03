package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ToppingRequestDto {
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isActive;
}
