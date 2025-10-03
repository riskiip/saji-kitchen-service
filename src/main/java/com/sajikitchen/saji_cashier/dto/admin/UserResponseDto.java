package com.sajikitchen.saji_cashier.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID userId;
    private String username;
    private String roleName;
    private boolean isActive;
}
