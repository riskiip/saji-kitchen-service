package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, UUID> {
}
