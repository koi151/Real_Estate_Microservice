package com.koi151.listing_services.model.dto;

import jakarta.persistence.EnumType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PostServicePricingDTO {
    private BigDecimal price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String packageType;
}
