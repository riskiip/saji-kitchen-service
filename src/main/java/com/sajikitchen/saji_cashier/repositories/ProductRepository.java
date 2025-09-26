package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
