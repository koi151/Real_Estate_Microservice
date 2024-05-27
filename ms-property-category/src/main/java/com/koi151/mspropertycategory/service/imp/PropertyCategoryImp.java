package com.koi151.mspropertycategory.service.imp;

import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;

import java.util.List;

public interface PropertyCategoryImp {

    List<PropertyCategoryHomeDTO> getCategoriesHomePage();
    List<PropertyCategoryHomeDTO> getCategories(String title); // !
    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
    boolean createCategory(PropertyCategoryRequest propertyCategoryRequest);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryRequest propertyCategory);
}
