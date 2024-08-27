package com.koi151.property_submissions.model.response;

public record PostServiceBasicInfoResponse(
    Long postServiceId,
    String name,
    Integer availableUnits
) {}
