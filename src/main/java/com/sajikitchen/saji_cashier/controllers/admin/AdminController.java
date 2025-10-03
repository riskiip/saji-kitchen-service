package com.sajikitchen.saji_cashier.controllers.admin;

import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;
import com.sajikitchen.saji_cashier.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDataDto> getDashboardData() {
        DashboardDataDto dashboardData = adminService.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }

    // Endpoint untuk CRUD Produk dan User akan ditambahkan di sini nanti
}
