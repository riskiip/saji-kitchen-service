package com.sajikitchen.saji_cashier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ToppingResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
