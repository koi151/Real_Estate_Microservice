package com.koi151.msproperties.repository.custom;

import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyCategoryRepositoryCustom {
    Page<PropertyCategoryEntity> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request, Pageable pageable);
}
