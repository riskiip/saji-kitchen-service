package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String username;
    private String password;
}