package com.koi151.notification.kafka.submission;

import java.math.BigDecimal;
import java.util.List;

public record PropertyServicePackage (
    Long propertyPostPackageId,
    String packageType,
    BigDecimal totalFee,
    List<PostServiceBasicInfo> postServiceBasicInfo
) {}
