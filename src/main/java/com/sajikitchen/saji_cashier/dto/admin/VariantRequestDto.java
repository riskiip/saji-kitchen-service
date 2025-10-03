package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VariantRequestDto {
    private String name;
    private BigDecimal price;
}
