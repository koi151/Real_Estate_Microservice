package com.koi151.listing_services.model.dto;

import jakarta.persistence.EnumType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PromotionDTO {
    private BigDecimal discountPercentage;
    private BigDecimal priceDiscount;
    private String packageType;
}
