package com.josecordoba.capitole.price;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    PriceService priceService;

    @InjectMocks
    PriceController priceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(priceController).build();
    }

    @ParameterizedTest
    @CsvSource({
            "2020-06-14T10:00:00,35455,1,1,35.5",
            "2020-06-14T16:00:00,35455,1,1,35.5",
            "2020-06-14T21:00:00,35455,1,1,35.5",
            "2020-06-15T10:00:00,35455,1,3,30.5",
            "2020-06-16T21:00:00,35455,1,4,38.95"
    })
    void testCalculatePriceScenarios(
            String applicationDate,
            Long productId,
            Long brandId,
            Long priceList,
            String expectedPrice
    ) throws Exception {
        PriceResponse expectedResponse = PriceResponse.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(priceList)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priceValue(new BigDecimal(expectedPrice))
                .build();

        // Mock the behavior of the priceService
        when(priceService.calculatePrice(Mockito.any())).thenReturn(Optional.ofNullable(expectedResponse));

        // Prepare the request payload
        PriceRequest request = PriceRequest.builder()
                .applicationDate(LocalDateTime.parse(applicationDate))
                .productId(productId)
                .brandId(brandId)
                .build();

        // Perform the POST request
        mockMvc.perform(post("/api/v3/prices/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.brandId").value(brandId))
                .andExpect(jsonPath("$.priceList").value(priceList))
                .andExpect(jsonPath("$.priceValue").value(expectedPrice))
                .andReturn();

        // Verify that the PriceService's calculatePrice method was called with the correct request
        verify(priceService).calculatePrice(request);
    }
}