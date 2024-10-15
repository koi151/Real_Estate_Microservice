package com.koi151.listing_services.model.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PostServiceBasicInfoDTO(
    Long postServiceId,
    BigDecimal price,
    String name,
    Integer availableUnits
) {}
