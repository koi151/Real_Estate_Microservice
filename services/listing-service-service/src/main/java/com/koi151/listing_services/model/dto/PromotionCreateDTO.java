package com.koi151.listing_services.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PromotionCreateDTO {
    private BigDecimal discountPercentage;
    private BigDecimal priceDiscount;
    private String packageType;
}
