package com.josecordoba.capitole.price;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v3/prices")
public class PriceController {

    @PostMapping("/calculate")
    public ResponseEntity<PriceResponse> calculatePrice(@RequestBody PriceRequest priceRequest) {
        // Dummy implementation to generate a PriceResponse
        PriceResponse priceResponse = PriceResponse.builder()
                .productId(priceRequest.getProductId())
                .brandId(priceRequest.getBrandId())
                .priceList(1L)
                .startDate(priceRequest.getApplicationDate())
                .endDate(priceRequest.getApplicationDate().plusDays(1))
                .priceValue(BigDecimal.valueOf(50.00))
                .build();

        return ResponseEntity.ok(priceResponse);
    }
}
