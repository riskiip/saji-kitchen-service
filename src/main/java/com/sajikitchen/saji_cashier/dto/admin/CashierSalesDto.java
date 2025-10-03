package com.sajikitchen.saji_cashier.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CashierSalesDto {
    private String cashierUsername;
    private BigDecimal totalSales;
}
