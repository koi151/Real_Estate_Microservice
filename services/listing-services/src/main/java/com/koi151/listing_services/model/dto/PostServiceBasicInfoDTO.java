package com.koi151.listing_services.model.dto;

public record PostServiceBasicInfoDTO(
    Long postServiceId,
    String name,
    Integer availableUnits
) {}
