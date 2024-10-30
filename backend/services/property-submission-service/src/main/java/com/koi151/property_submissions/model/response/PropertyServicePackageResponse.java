package com.koi151.property_submissions.model.response;

import java.math.BigDecimal;
import java.util.List;

public record PropertyServicePackageResponse (
    Long propertyPostPackageId,
    String packageType,
    BigDecimal totalFee,
    List<PostServiceBasicInfoResponse> postServiceBasicInfo
){}
