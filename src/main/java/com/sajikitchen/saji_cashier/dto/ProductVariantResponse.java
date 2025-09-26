package com.sajikitchen.saji_cashier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductVariantResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String productName;
}
