package com.sajikitchen.saji_cashier.controllers.admin;

import com.sajikitchen.saji_cashier.dto.admin.*;
import com.sajikitchen.saji_cashier.models.Product;
import com.sajikitchen.saji_cashier.models.ProductVariant;
import com.sajikitchen.saji_cashier.models.Topping;
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

    @PostMapping("/products/{productId}/variants")
    public ResponseEntity<ProductVariant> createVariant(
            @PathVariable UUID productId,
            @RequestBody VariantRequestDto request) {
        ProductVariant newVariant = adminService.createVariant(productId, request);
        return new ResponseEntity<>(newVariant, HttpStatus.CREATED);
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ProductVariant> updateVariant(
            @PathVariable UUID variantId,
            @RequestBody VariantRequestDto request) {
        ProductVariant updatedVariant = adminService.updateVariant(variantId, request);
        return ResponseEntity.ok(updatedVariant);
    }

    // ==========================================================
    // ENDPOINT UNTUK MANAJEMEN TOPPING
    // ==========================================================
    @PostMapping("/toppings")
    public ResponseEntity<Topping> createTopping(@RequestBody ToppingRequestDto request) {
        Topping newTopping = adminService.createTopping(request);
        return new ResponseEntity<>(newTopping, HttpStatus.CREATED);
    }

    @PutMapping("/toppings/{toppingId}")
    public ResponseEntity<Topping> updateTopping(
            @PathVariable UUID toppingId,
            @RequestBody ToppingRequestDto request) {
        Topping updatedTopping = adminService.updateTopping(toppingId, request);
        return ResponseEntity.ok(updatedTopping);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllCashiers() {
        return ResponseEntity.ok(adminService.findAllCashiers());
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createCashier(@RequestBody CreateUserRequestDto request) {
        UserResponseDto newCashier = adminService.createCashier(request);
        return new ResponseEntity<>(newCashier, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> updateCashier(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRequestDto request) {
        UserResponseDto updatedCashier = adminService.updateCashier(userId, request);
        return ResponseEntity.ok(updatedCashier);
    }
}
