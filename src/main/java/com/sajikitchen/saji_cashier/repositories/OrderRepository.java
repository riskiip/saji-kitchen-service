package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.customer " +
            "JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.productVariant pv " +
            "LEFT JOIN FETCH pv.product " +
            "LEFT JOIN FETCH oi.topping " +
            "WHERE o.orderId = :orderId")
    Optional<Order> findByIdWithDetails(String orderId);
}
