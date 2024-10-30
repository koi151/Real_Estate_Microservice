package com.koi151.msproperty.repository.custom;

import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyCategoryRepositoryCustom {
    Page<PropertyCategory> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request, Pageable pageable);
}
