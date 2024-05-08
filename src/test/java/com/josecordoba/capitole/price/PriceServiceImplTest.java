package com.josecordoba.capitole.price;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceServiceImpl priceService;

    @Test
    void testCalculatePrice_ReturnsPriceResponse_WhenPriceFound() {
        // Test case 1: Price found for the given product, brand, and application date
        PriceRequest request = new PriceRequest();
        request.setProductId(35455L);
        request.setBrandId(1L);
        request.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

        Price price = new Price();
        price.setProductId(35455L);
        price.setBrandId(1L);
        price.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        price.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        price.setPriceList(1L);
        price.setPriority(1);
        price.setPriceValue(new BigDecimal("35.5"));
        price.setCurrency("EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate()))
                .thenReturn(Collections.singletonList(price));

        Optional<PriceResponse> response = priceService.calculatePrice(request);

        assertTrue(response.isPresent());
        assertEquals(35455L, response.get().getProductId());
        assertEquals(1L, response.get().getBrandId());
        assertEquals(1L, response.get().getPriceList());
        assertEquals(LocalDateTime.of(2020, 6, 14, 0, 0), response.get().getStartDate());
        assertEquals(LocalDateTime.of(2020, 12, 31, 23, 59, 59), response.get().getEndDate());
        assertEquals(new BigDecimal("35.5"), response.get().getPriceValue());

        verify(priceRepository).findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate());
    }

    @Test
    void testCalculatePrice_ReturnsEmptyOptional_WhenPriceNotFound() {
        // Test case 2: No price found for the given product, brand, and application date
        PriceRequest request = new PriceRequest();
        request.setProductId(9999L); // Non-existent product
        request.setBrandId(1L);
        request.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate()))
                .thenReturn(Collections.emptyList());

        Optional<PriceResponse> response = priceService.calculatePrice(request);

        assertFalse(response.isPresent());

        verify(priceRepository).findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate());
    }

    @Test
    void testCalculatePrice_ReturnsPriceResponse_WhenMultiplePricesFound() {
        // Test case 3: Multiple prices found for the given product, brand, and application date
        PriceRequest request = new PriceRequest();
        request.setProductId(35455L);
        request.setBrandId(1L);
        request.setApplicationDate(LocalDateTime.of(2020, 6, 14, 16, 0)); // Request at 16:00 on June 14th

        // Create multiple prices with different priorities
        Price price1 = new Price();
        price1.setProductId(35455L);
        price1.setBrandId(1L);
        price1.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        price1.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        price1.setPriceList(1L);
        price1.setPriority(1);
        price1.setPriceValue(new BigDecimal("35.5"));
        price1.setCurrency("EUR");

        Price price2 = new Price();
        price2.setProductId(35455L);
        price2.setBrandId(1L);
        price2.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0));
        price2.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30));
        price2.setPriceList(2L);
        price2.setPriority(2);
        price2.setPriceValue(new BigDecimal("25.45"));
        price2.setCurrency("EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate()))
                .thenReturn(Arrays.asList(price2, price1));

        Optional<PriceResponse> response = priceService.calculatePrice(request);

        assertTrue(response.isPresent());
        assertEquals(35455L, response.get().getProductId());
        assertEquals(1L, response.get().getBrandId());
        assertEquals(2L, response.get().getPriceList()); // Price with higher priority should be selected
        assertEquals(LocalDateTime.of(2020, 6, 14, 15, 0), response.get().getStartDate());
        assertEquals(LocalDateTime.of(2020, 6, 14, 18, 30), response.get().getEndDate());
        assertEquals(new BigDecimal("25.45"), response.get().getPriceValue());

        verify(priceRepository).findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                request.getProductId(), request.getBrandId(), request.getApplicationDate(), request.getApplicationDate());
    }
}
