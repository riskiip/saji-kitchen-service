package com.sajikitchen.saji_cashier.controllers.admin;

import com.sajikitchen.saji_cashier.dto.admin.DailySalesDetailDto;
import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;
import com.sajikitchen.saji_cashier.dto.admin.ProductRequestDto;
import com.sajikitchen.saji_cashier.models.Product;
import com.sajikitchen.saji_cashier.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto request) {
        Product newProduct = adminService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID productId, @RequestBody ProductRequestDto request) {
        Product updatedProduct = adminService.updateProduct(productId, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        adminService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
