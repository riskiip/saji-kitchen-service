package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Spring Data JPA akan otomatis membuat query untuk mencari customer berdasarkan kolom email
    Optional<Customer> findByEmail(String email);
}
