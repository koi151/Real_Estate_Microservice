package com.koi151.property_submissions.model.response;

import java.math.BigDecimal;

public record PostServiceBasicInfoResponse(
    BigDecimal price,
    Long postServiceId,
    String name,
    Integer availableUnits
) {}
