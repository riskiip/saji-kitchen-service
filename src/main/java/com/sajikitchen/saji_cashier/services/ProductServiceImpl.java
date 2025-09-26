package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.ProductVariantResponse;
import com.sajikitchen.saji_cashier.repositories.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductVariantRepository productVariantRepository;

    public ProductServiceImpl(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public List<ProductVariantResponse> getActiveProductVariants() {
        return productVariantRepository.findAll().stream()
                // Filter hanya untuk produk yang aktif
                .filter(variant -> variant.getProduct().getIsActive())
                .map(variant -> new ProductVariantResponse(
                        variant.getVariantId(),
                        variant.getName(),
                        variant.getPrice(),
                        variant.getProduct().getName()
                ))
                .collect(Collectors.toList());
    }
}
