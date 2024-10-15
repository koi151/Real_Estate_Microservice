package com.koi151.listing_services.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PostServicePricingCreateDTO {
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String packageType;
}
