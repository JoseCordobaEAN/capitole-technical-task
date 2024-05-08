package com.josecordoba.capitole.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v3/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<PriceResponse> calculatePrice(@RequestBody PriceRequest priceRequest) {
        return priceService.calculatePrice(priceRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
