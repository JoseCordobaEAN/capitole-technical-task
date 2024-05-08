package com.josecordoba.capitole.price;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "PRICES")
public class Price {

    @Id
    @GeneratedValue
    private Long id;
    private Long brandId;
    private Long productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long priceList;
    private Integer priority;
    @Column(name = "PRICE")
    private BigDecimal priceValue;
    @Column(name = "CURR")
    private String currency;

}
