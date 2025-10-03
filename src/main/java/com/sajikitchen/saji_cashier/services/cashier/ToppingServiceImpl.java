package com.sajikitchen.saji_cashier.services.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ToppingResponse;
import com.sajikitchen.saji_cashier.repositories.ToppingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToppingServiceImpl implements ToppingService {

    private final ToppingRepository toppingRepository;

    public ToppingServiceImpl(ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @Override
    public List<ToppingResponse> getActiveToppings() {
        return toppingRepository.findAll().stream()
                // Filter hanya topping yang aktif
                .filter(topping -> topping.isActive())
                .map(topping -> ToppingResponse.builder()
                        .toppingId(topping.getToppingId())
                        .name(topping.getName())
                        .price(topping.getPrice())
                        .imageUrl(topping.getImageUrl())
                        .isActive(topping.isActive())
                        .build())
                .collect(Collectors.toList());
    }
}
