package com.sajikitchen.saji_cashier.repositories;

import com.sajikitchen.saji_cashier.models.ResponseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseCodeRepository extends JpaRepository<ResponseCode, String> {
}
