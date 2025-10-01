package com.sajikitchen.saji_cashier.controllers;

import com.sajikitchen.saji_cashier.dto.ProductResponse;
import com.sajikitchen.saji_cashier.dto.ToppingResponse;
import com.sajikitchen.saji_cashier.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu") // <-- Kuncinya ada di sini, harus ada '/menu'
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/products") // URL final: /api/v1/menu/products
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(menuService.getAllProducts());
    }

    @GetMapping("/toppings") // URL final: /api/v1/menu/toppings
    public ResponseEntity<List<ToppingResponse>> getAllToppings() {
        return ResponseEntity.ok(menuService.getAllToppings());
    }
}