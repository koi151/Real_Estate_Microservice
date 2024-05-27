package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.client.PropertiesClient;
import com.koi151.mspropertycategory.dto.PropertyCategoryDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyCategoryService implements PropertyCategoryImp {

    @Autowired
    PropertyCategoryRepository propertyCategoryRepository;

    @Autowired
    PropertiesClient propertiesClient;

    @Override
    public List<PropertyCategoryDTO> getCategories(String title) {
        List<PropertyCategory> propertyCategories = propertyCategoryRepository.findByTitleContainingIgnoreCase(title);
        List<PropertyCategoryDTO> propertyCategoryDTOList = new ArrayList<>();

        for (PropertyCategory categoryDTO: propertyCategories) {
            PropertyCategoryDTO propertyCategoryDTO = new PropertyCategoryDTO();
            propertyCategoryDTO.setTitle(categoryDTO.getTitle());
            propertyCategoryDTOList.add(propertyCategoryDTO);
        }

        return propertyCategoryDTOList;
    }

    @Override
    public FullCategoryResponse findCategoryWithProperties(Integer categoryId) {
        var category = propertyCategoryRepository.findById(categoryId)
                .orElse(
                        PropertyCategory.builder()
                                .title("NOT FOUND")
                                .images("NOT FOUND")
                                .description("NOT FOUND")
                                .build()
                );


        List<Properties> properties = propertiesClient.findAllPropertiesByCategory(categoryId);

        return FullCategoryResponse.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .status(category.getStatus())
                .properties(properties)
                .build();
    }

    @Override
    public PropertyCategoryTitleDTO getCategoryTitleById(Integer id) {
        PropertyCategoryTitleDTO propertyCategoryTitleDTO = new PropertyCategoryTitleDTO();
        PropertyCategory propertyCategory = propertyCategoryRepository.findById(id)
                .orElse(PropertyCategory.builder()
                        .title("NOT_FOUND")
                        .build());

        propertyCategoryTitleDTO.setTitle(propertyCategory.getTitle());
        return propertyCategoryTitleDTO;
    }

    @Override
    public List<PropertyCategoryDTO> getCategoriesHomePage() {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategory> categories = propertyCategoryRepository.findAll(pageRequest);

        List<PropertyCategoryDTO> propertyCategoryDTOList = new ArrayList<>();

        for (PropertyCategory category: categories) {
            PropertyCategoryDTO propertyCategoryDTO = new PropertyCategoryDTO();

            propertyCategoryDTO.setTitle(category.getTitle());
            propertyCategoryDTO.setDescription(category.getDescription());
            propertyCategoryDTO.setImages(category.getImages());

            propertyCategoryDTOList.add((propertyCategoryDTO));
        }

        return propertyCategoryDTOList;
    }

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

