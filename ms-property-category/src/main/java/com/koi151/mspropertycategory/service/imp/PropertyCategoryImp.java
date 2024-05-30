package com.koi151.mspropertycategory.service.imp;

import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import customExceptions.CategoryNotFoundException;
import customExceptions.FieldRequiredException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyCategoryImp {

    List<PropertyCategoryHomeDTO> getCategoriesHomePage();
    List<PropertyCategoryHomeDTO> getCategories(String title); // !
    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
    PropertyCategory createCategory(PropertyCategoryRequest request);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryRequest request)
            throws FieldRequiredException;
    void deleteCategory(Integer id) throws CategoryNotFoundException;
}

