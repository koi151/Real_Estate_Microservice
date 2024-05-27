package com.koi151.mspropertycategory.service.imp;

import com.koi151.mspropertycategory.dto.PropertyCategoryDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;

import java.util.List;

public interface PropertyCategoryImp {

    List<PropertyCategoryDTO> getCategoriesHomePage();
    List<PropertyCategoryDTO> getCategories(String title);
    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
    boolean createCategory(PropertyCategoryRequest propertyCategoryRequest);

}
