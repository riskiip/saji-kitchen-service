package com.sajikitchen.saji_cashier.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Penting untuk query JPQL
public class DailySalesDetailDto {
    private String productName;
    private String variantName;
    private String toppingName; // Akan bernilai null jika tidak ada topping
    private Long totalQuantity;
}
