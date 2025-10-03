package com.sajikitchen.saji_cashier.services.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ProductResponse;
import com.sajikitchen.saji_cashier.dto.cashier.ProductVariantResponse;
import com.sajikitchen.saji_cashier.dto.cashier.ToppingResponse;
import com.sajikitchen.saji_cashier.models.Product;
import com.sajikitchen.saji_cashier.models.Topping;
import com.sajikitchen.saji_cashier.repositories.ProductRepository;
import com.sajikitchen.saji_cashier.repositories.ToppingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final ProductRepository productRepository;
    private final ToppingRepository toppingRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ToppingResponse> getAllToppings() {
        return toppingRepository.findAll().stream()
                .map(this::mapToppingToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapProductToResponse(Product product) {
        List<ProductVariantResponse> variantResponses = product.getVariants().stream()
                .map(variant -> ProductVariantResponse.builder()
                        .variantId(variant.getVariantId())
                        .variantName(variant.getName())
                        .price(variant.getPrice())
                        .build())
                .collect(Collectors.toList());

        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .isActive(product.isActive())
                .variants(variantResponses)
                .build();
    }

    private ToppingResponse mapToppingToResponse(Topping topping) {
        return ToppingResponse.builder()
                .toppingId(topping.getToppingId())
                .name(topping.getName())
                .price(topping.getPrice())
                .imageUrl(topping.getImageUrl())
                .isActive(topping.isActive())
                .build();
    }
}
