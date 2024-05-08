package com.josecordoba.capitole.price;

import java.util.Optional;

public interface PriceService {
    Optional<PriceResponse> calculatePrice(PriceRequest request);
}
