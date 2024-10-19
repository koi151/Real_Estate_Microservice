package com.koi151.msproperty.service;

import com.koi151.msproperty.customExceptions.CategoryNotFoundException;
import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryDetailDTO;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryTitleDTO;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyCategoryService {

    Page<PropertyCategoryHomeDTO> getCategoriesHomePage(PropertyCategorySearchRequest request, Pageable pageable);
    List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum statusEnum);
    PropertyCategory getCategoryById(Integer id);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
//    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategory createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> images);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryUpdateRequest request, List<MultipartFile> files);
    void deleteCategory(Integer id) throws CategoryNotFoundException;
}

