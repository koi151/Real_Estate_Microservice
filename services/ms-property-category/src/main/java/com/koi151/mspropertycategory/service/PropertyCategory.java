package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.PropertyCategoryEntity;
import com.koi151.mspropertycategory.entity.StatusEnum;
import com.koi151.mspropertycategory.model.request.PropertyCategorySearchRequest;
import com.koi151.mspropertycategory.model.response.FullCategoryResponse;
import com.koi151.mspropertycategory.model.request.PropertyCategoryCreateRequest;
import com.koi151.mspropertycategory.model.request.PropertyCategoryUpdateRequest;
import com.koi151.mspropertycategory.model.response.PropertyCategorySearchResponse;
import customExceptions.CategoryNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyCategory {

    List<PropertyCategoryHomeDTO> getCategoriesHomePage();
    List<PropertyCategoryHomeDTO> getCategoriesByTitle(String title);
    List<PropertyCategorySearchResponse> findAllPropertyCategories(PropertyCategorySearchRequest request);
    List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum statusEnum);
    PropertyCategoryEntity getCategoryById(Integer id);
    PropertyCategoryTitleDTO getCategoryTitleById(Integer id);
    FullCategoryResponse findCategoryWithProperties(Integer categoryId);
    PropertyCategoryEntity createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> images);
    PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryUpdateRequest request, List<MultipartFile> files);
    void deleteCategory(Integer id) throws CategoryNotFoundException;
}

