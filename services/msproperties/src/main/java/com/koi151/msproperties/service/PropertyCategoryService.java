package com.koi151.msproperties.service;

import com.koi151.msproperties.customExceptions.CategoryNotFoundException;
import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryDetailDTO;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryTitleDTO;
import com.koi151.msproperties.model.reponse.PropertyCategorySearchResponse;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyCategoryService {

    List<PropertyCategoryHomeDTO> getCategoriesHomePage();
    List<PropertyCategoryHomeDTO> getCategoriesByTitle(String title);
    List<PropertyCategorySearchResponse> findAllPropertyCategories(PropertyCategorySearchRequest request);
    List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum statusEnum);
    PropertyCategoryEntity getCategoryById(Integer id);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
//    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategoryEntity createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> images);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryUpdateRequest request, List<MultipartFile> files);
    void deleteCategory(Integer id) throws CategoryNotFoundException;
}

