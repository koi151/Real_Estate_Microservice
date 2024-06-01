package com.koi151.mspropertycategory.service.imp;

import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import customExceptions.CategoryNotFoundException;
import customExceptions.FieldRequiredException;

import java.util.List;

public interface PropertyCategoryImp {

    List<PropertyCategoryHomeDTO> getCategoriesHomePage();
    List<PropertyCategoryHomeDTO> getCategories(String title);
    List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum statusEnum);
    PropertyCategory getCategoryById(Integer id);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategory createCategory(PropertyCategoryRequest request);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryRequest request);
    void deleteCategory(Integer id) throws CategoryNotFoundException;
}

