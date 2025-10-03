package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardDataDto {
    private BigDecimal todayRevenue;
    private List<DailySalesDto> dailySales;
    private List<CashierSalesDto> salesByCashier;
}