package com.koi151.property_submissions.model.request;

import lombok.Builder;

@Builder
public record PropertyServicePackageSearchRequest(
    Long packageId,
    Long propertyId
) {}
