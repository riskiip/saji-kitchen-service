package com.sajikitchen.saji_cashier.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "response_codes")
@Getter
@Setter
public class ResponseCode {

    @Id
    private String code;

    @Column(name = "message_en", nullable = false)
    private String messageEn;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    private String description;
}