package com.sajikitchen.saji_cashier.controllers.admin;

import com.sajikitchen.saji_cashier.dto.admin.DailySalesDetailDto;
import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;
import com.sajikitchen.saji_cashier.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/dashboard/sales-detail")
    public ResponseEntity<List<DailySalesDetailDto>> getSalesDetailByDate(
            @RequestParam(value = "date", required = false) String dateStr) {

        // Jika parameter tanggal tidak disediakan, gunakan tanggal hari ini
        String dateToQuery = (dateStr == null || dateStr.isEmpty())
                ? java.time.LocalDate.now().toString()
                : dateStr;

        List<DailySalesDetailDto> salesDetail = adminService.getDailySalesDetail(dateToQuery);
        return ResponseEntity.ok(salesDetail);
    }
    // Endpoint untuk CRUD Produk dan User akan ditambahkan di sini nanti
}
