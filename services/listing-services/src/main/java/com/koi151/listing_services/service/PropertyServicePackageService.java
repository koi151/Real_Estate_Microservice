package com.koi151.listing_services.service;

import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.model.request.PropertyServicePackageSearchRequest;

public interface PropertyServicePackageService {
    PropertyServicePackageCreateDTO createPropertyServicePackage(PropertyServicePackageCreateRequest request);
    PropertyServicePackageSummaryDTO findPropertyServicePackageByCriteria(PropertyServicePackageSearchRequest request);
}