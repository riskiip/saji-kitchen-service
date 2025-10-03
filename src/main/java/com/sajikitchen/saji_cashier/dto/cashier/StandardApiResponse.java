package com.sajikitchen.saji_cashier.dto.cashier;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardApiResponse<T> {
    @JsonProperty("error_schema")
    private ErrorSchema errorSchema;

    @JsonProperty("output_schema")
    private T outputSchema;
}
