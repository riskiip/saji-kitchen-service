package com.sajikitchen.saji_cashier.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID customerId;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
