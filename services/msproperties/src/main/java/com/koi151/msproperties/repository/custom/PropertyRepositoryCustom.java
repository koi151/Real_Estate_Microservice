package com.koi151.msproperties.repository.custom;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PropertyRepositoryCustom {
    Page<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request, Pageable pageable);
}