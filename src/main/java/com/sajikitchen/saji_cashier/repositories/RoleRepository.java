package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
