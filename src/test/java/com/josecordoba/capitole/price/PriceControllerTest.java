package com.josecordoba.capitole.price;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceRepository priceRepository;

    @ParameterizedTest
    @CsvSource({
            "2020-06-14T10:00:00,35455,1,1,35.5,2020-06-14T00:00:00,2020-12-31T23:59:59",
            "2020-06-14T16:00:00,35455,1,2,25.45,2020-06-14T15:00:00,2020-06-14T18:30:00",
            "2020-06-14T21:00:00,35455,1,1,35.5,2020-06-14T00:00:00,2020-12-31T23:59:59",
            "2020-06-15T10:00:00,35455,1,3,30.5,2020-06-15T00:00:00,2020-06-15T11:00:00",
            "2020-06-16T21:00:00,35455,1,4,38.95,2020-06-15T16:00:00,2020-12-31T23:59:59"
    })
    void testCalculatePrice(
            String applicationDate,
            Long productId,
            Long brandId,
            Long priceList,
            String expectedPrice,
            String expectedStartDate,
            String expectedEndDate
    ) throws Exception {
        PriceRequest request = PriceRequest.builder()
                .applicationDate(LocalDateTime.parse(applicationDate))
                .productId(productId)
                .brandId(brandId)
                .build();

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
                .andExpect(jsonPath("$.startDate").value(expectedStartDate))
                .andExpect(jsonPath("$.endDate").value(expectedEndDate))
                .andExpect(jsonPath("$.priceValue").value(expectedPrice)) // Use expectedPrice from the CSV source
                .andReturn();
    }
}
