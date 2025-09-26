package com.sajikitchen.saji_cashier.services;

import com.sajikitchen.saji_cashier.dto.ToppingResponse;
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
                .filter(topping -> topping.getIsActive())
                .map(topping -> new ToppingResponse(
                        topping.getToppingId(),
                        topping.getName(),
                        topping.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
