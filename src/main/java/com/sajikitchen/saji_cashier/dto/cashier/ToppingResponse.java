package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ToppingResponse {
    private UUID toppingId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private boolean isActive;
}
