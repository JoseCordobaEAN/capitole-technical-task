package com.josecordoba.capitole.price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {

    private LocalDateTime applicationDate;
    private Long productId;
    private Long brandId;
}
