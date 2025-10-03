package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private String productName;
    private String variantName;
    private String toppingName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal toppingPrice;
    private BigDecimal subTotal;
}
