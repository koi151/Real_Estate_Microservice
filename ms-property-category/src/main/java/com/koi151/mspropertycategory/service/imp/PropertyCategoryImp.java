package com.koi151.mspropertycategory.service.imp;

import com.koi151.mspropertycategory.dto.PropertyCategoryDTO;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;

import java.util.List;

public interface PropertyCategoryImp {

    List<PropertyCategoryDTO> getCategoriesHomePage();
    boolean createCategory(PropertyCategoryRequest propertyCategoryRequest);

}
