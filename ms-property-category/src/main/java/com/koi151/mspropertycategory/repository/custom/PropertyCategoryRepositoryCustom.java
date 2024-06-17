package com.koi151.mspropertycategory.repository.custom;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.model.request.PropertyCategorySearchRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PropertyCategoryRepositoryCustom {
    List<PropertyCategory> getPropertyCategoryByCriterias (PropertyCategorySearchRequest request);
}
