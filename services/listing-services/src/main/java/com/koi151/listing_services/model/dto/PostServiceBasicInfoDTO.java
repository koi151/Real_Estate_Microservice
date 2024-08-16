package com.koi151.listing_services.model.dto;

public record PostServiceBasisInfoDTO (
    Long postServiceId,
    String name,
    Integer availableUnits
) {}
