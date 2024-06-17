package com.koi151.mspropertycategory.repository.custom;

import com.koi151.mspropertycategory.entity.PropertyCategoryEntity;
import com.koi151.mspropertycategory.model.request.PropertyCategorySearchRequest;

import java.util.List;

public interface PropertyCategoryRepositoryCustom {
    List<PropertyCategoryEntity> getPropertyCategoryByCriterias (PropertyCategorySearchRequest request);
}
