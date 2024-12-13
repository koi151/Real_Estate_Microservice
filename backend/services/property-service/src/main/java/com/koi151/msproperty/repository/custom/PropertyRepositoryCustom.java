package com.koi151.msproperty.repository.custom;

import com.koi151.msproperty.model.projection.PropertySearchProjection;
import com.koi151.msproperty.model.request.property.PropertySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyRepositoryCustom {
    Page<PropertySearchProjection> findPropertiesForAdmin(PropertySearchRequest request, Pageable pageable);
    Page<PropertySearchProjection> findPropertiesForClient(PropertySearchRequest request, Pageable pageable);
}

