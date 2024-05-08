package com.josecordoba.capitole.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Optional<PriceResponse> calculatePrice(PriceRequest request) {

        Optional<Price> price =
                priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                        request.getProductId(),
                                request.getBrandId(),
                                request.getApplicationDate(),
                                request.getApplicationDate()
                        )
                        .stream().findFirst();


        return price.map(this::mapToPriceResponse);
    }

    private PriceResponse mapToPriceResponse(Price price) {
        return PriceResponse.builder()
                .productId(price.getProductId())
                .brandId(price.getBrandId())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .priceValue(price.getPriceValue())
                .build();
    }
}
