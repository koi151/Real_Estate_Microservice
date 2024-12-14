package com.koi151.msproperty.repository.custom;

import com.koi151.msproperty.model.projection.PropertySearchProjection;
import com.koi151.msproperty.model.request.property.PropertyFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyRepositoryCustom {
    Page<PropertySearchProjection> findPropertiesForAdmin(PropertyFilterRequest request, Pageable pageable);
    Page<PropertySearchProjection> findPropertiesForClient(PropertyFilterRequest request, Pageable pageable);
}

