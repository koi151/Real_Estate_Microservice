package com.koi151.listing_services.model.request;

public record PropertyServicePackageSearchRequest (
    Long packageId,
    Long propertyId
) {}
