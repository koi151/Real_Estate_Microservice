package com.koi151.msproperties.repository.custom;

import com.koi151.msproperties.model.projection.PropertySearchProjection;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyRepositoryCustom {
    Page<Tuple> findPropertiesByCriteria(PropertySearchRequest request, Pageable pageable);
}

