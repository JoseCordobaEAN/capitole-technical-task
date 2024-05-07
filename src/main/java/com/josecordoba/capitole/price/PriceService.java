package com.josecordoba.capitole.price;

import org.springframework.http.ResponseEntity;

public interface PriceService {
    ResponseEntity<PriceResponse> calculatePrice(PriceRequest request);
}
