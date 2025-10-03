package com.sajikitchen.saji_cashier.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailySalesDto {
    private LocalDate date;
    private BigDecimal totalSales;
}