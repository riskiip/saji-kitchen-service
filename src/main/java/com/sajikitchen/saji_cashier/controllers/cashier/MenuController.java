package com.sajikitchen.saji_cashier.controllers.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ProductResponse;
import com.sajikitchen.saji_cashier.dto.cashier.ToppingResponse;
import com.sajikitchen.saji_cashier.services.cashier.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
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