package com.koi151.notification.kafka.submission;

import java.math.BigDecimal;

public record PostServiceBasicInfo (
    Long postServiceId,
    BigDecimal price,
    String name,
    Integer availableUnits
) {}
