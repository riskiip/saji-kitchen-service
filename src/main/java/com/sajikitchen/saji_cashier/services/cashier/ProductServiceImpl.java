package com.sajikitchen.saji_cashier.services.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ProductVariantResponse;
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
                // PERBAIKAN #1: Gunakan isActive()
                .filter(variant -> variant.getProduct().isActive())
                // PERBAIKAN #2: Gunakan builder untuk membuat DTO
                .map(variant -> ProductVariantResponse.builder()
                        .variantId(variant.getVariantId())
                        .variantName(variant.getName()) // Nama dari variant
                        .price(variant.getPrice())
                        .productName(variant.getProduct().getName()) // Nama dari product induk
                        .imageUrl(variant.getProduct().getImageUrl())
                        .description(variant.getProduct().getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
