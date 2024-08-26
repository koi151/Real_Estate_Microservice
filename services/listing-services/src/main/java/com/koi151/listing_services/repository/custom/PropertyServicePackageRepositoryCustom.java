package com.koi151.listing_services.repository.custom;

import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageSearchRequest;

public interface PropertyServicePackageRepositoryCustom {
    PropertyServicePackageSummaryDTO findPropertyServicePackageByCriteria(PropertyServicePackageSearchRequest request);

}
