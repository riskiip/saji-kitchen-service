package com.sajikitchen.saji_cashier.services.admin;

import com.sajikitchen.saji_cashier.dto.admin.DailySalesDetailDto;
import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;

import java.util.List;

public interface AdminService {
    DashboardDataDto getDashboardData();
    List<DailySalesDetailDto> getDailySalesDetail(String date);
}