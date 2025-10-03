package com.sajikitchen.saji_cashier.controllers.cashier;

import com.sajikitchen.saji_cashier.dto.cashier.ErrorSchema;
import com.sajikitchen.saji_cashier.dto.cashier.StandardApiResponse;
import com.sajikitchen.saji_cashier.dto.cashier.ToppingResponse;
import com.sajikitchen.saji_cashier.services.cashier.ToppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/toppings")
public class ToppingController {

    private final ToppingService toppingService;

    public ToppingController(ToppingService toppingService) {
        this.toppingService = toppingService;
    }

    @GetMapping
    public ResponseEntity<StandardApiResponse<List<ToppingResponse>>> getAllToppings() {
        List<ToppingResponse> toppings = toppingService.getActiveToppings();

        StandardApiResponse<List<ToppingResponse>> response = new StandardApiResponse<>();
        ErrorSchema.ErrorMessage message = new ErrorSchema.ErrorMessage("Success", "Berhasil");
        ErrorSchema errorSchema = new ErrorSchema("SAJI-00-001", message);

        response.setErrorSchema(errorSchema);
        response.setOutputSchema(toppings);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
