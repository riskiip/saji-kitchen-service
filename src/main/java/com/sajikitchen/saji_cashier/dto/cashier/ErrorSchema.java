package com.sajikitchen.saji_cashier.dto.cashier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorSchema {
    private String errorCode;
    private ErrorMessage errorMessage;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorMessage {
        private String english;
        private String indonesia;
    }
}
