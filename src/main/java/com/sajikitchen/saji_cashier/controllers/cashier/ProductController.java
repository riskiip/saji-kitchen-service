package com.sajikitchen.saji_cashier.controllers.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ErrorSchema;
import com.sajikitchen.saji_cashier.dto.cashier.ProductVariantResponse;
import com.sajikitchen.saji_cashier.dto.cashier.StandardApiResponse;
import com.sajikitchen.saji_cashier.services.cashier.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<StandardApiResponse<List<ProductVariantResponse>>> getAllProducts() {
        List<ProductVariantResponse> products = productService.getActiveProductVariants();

        StandardApiResponse<List<ProductVariantResponse>> response = new StandardApiResponse<>();
        ErrorSchema.ErrorMessage message = new ErrorSchema.ErrorMessage("Success", "Berhasil");
        ErrorSchema errorSchema = new ErrorSchema("SAJI-00-001", message);

        response.setErrorSchema(errorSchema);
        response.setOutputSchema(products);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
