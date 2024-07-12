package com.koi151.msproperties.repository.custom;

import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;

import java.util.List;

public interface PropertyCategoryRepositoryCustom {
    List<PropertyCategoryEntity> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request);
}
