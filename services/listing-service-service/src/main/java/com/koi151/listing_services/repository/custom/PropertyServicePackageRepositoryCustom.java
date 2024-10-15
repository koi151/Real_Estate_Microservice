package com.koi151.listing_services.repository.custom;

import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;

import java.util.Map;

public interface PropertyServicePackageRepositoryCustom {
    PropertyServicePackageSummaryDTO findPropertyServicePackageByCriteria(Map<String, String> params);

}
