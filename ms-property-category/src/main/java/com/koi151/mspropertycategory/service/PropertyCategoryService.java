package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PropertyCategoryService implements PropertyCategoryImp {

    @Autowired
    PropertyCategoryRepository propertyCategoryRepository;

    @Override
    public boolean createCategory(PropertyCategoryRequest propertyCategoryRequest) {
        boolean isInsertSuccess = false;

        try {
            // add saving images later

            PropertyCategory propertyCategory = new PropertyCategory();

            propertyCategory.setTitle(propertyCategoryRequest.getTitle());
            propertyCategory.setDescription(propertyCategoryRequest.getDescription());
            propertyCategory.setStatus(propertyCategoryRequest.getStatus());
            propertyCategory.setDeleted(false);
            propertyCategory.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            propertyCategoryRepository.save(propertyCategory);
            isInsertSuccess = true;

        } catch (Exception e) {
            System.out.println("Error occurred while creating category: " + e.getMessage());
        }

        return isInsertSuccess;
    }
}

