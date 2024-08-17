package com.koi151.listing_services.repository.custom;

import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;

public interface PropertyServicePackageRepositoryCustom {
    PropertyServicePackageSummaryDTO findPropertyServicePackageWithsPostServices(Long id);
}
