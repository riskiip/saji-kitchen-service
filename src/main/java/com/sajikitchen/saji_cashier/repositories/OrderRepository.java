package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
