package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.client.PropertiesClient;
import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
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
    public List<PropertyCategoryHomeDTO> getCategories(String title) {
        List<PropertyCategory> propertyCategories = propertyCategoryRepository.findByTitleContainingIgnoreCase(title);
        List<PropertyCategoryHomeDTO> propertyCategoryDTOList = new ArrayList<>();

        for (PropertyCategory categoryDTO: propertyCategories) {
            PropertyCategoryHomeDTO propertyCategoryDTO = new PropertyCategoryHomeDTO();
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
    public List<PropertyCategoryHomeDTO> getCategoriesHomePage() {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategory> categories = propertyCategoryRepository.findAll(pageRequest);

        List<PropertyCategoryHomeDTO> propertyCategoryDTOList = new ArrayList<>();

        for (PropertyCategory category: categories) {
            PropertyCategoryHomeDTO propertyCategoryDTO = new PropertyCategoryHomeDTO();

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

    @Override
    public PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryRequest categoryRequest) {
        return propertyCategoryRepository.findById(id)
                .map(existingCategory -> {
                    if (categoryRequest.getTitle() != null)
                        existingCategory.setTitle(categoryRequest.getTitle());
                    if (categoryRequest.getDescription() != null)
                        existingCategory.setDescription(categoryRequest.getDescription());
                    if (categoryRequest.getImages() != null)
                        existingCategory.setImages(categoryRequest.getImages());
                    if (categoryRequest.getStatus() != null)
                        existingCategory.setStatus(categoryRequest.getStatus());

                    return propertyCategoryRepository.save(existingCategory);
                })
                .map(PropertyCategoryDetailDTO::new) // Map to DTO after saving
                .orElseThrow(() -> new RuntimeException("Property category not found with id: " + id));
    }



    // Helper method to map PropertyCategory entity to PropertyCategoryDetailDTO
    private PropertyCategoryDetailDTO mapToDetailDTO(PropertyCategory category) {
        PropertyCategoryDetailDTO categoryDetailDTO = new PropertyCategoryDetailDTO();
        categoryDetailDTO.setTitle(category.getTitle());
        categoryDetailDTO.setImages(category.getImages());
        categoryDetailDTO.setDescription(category.getDescription());
        categoryDetailDTO.setStatus(category.getStatus());
        return categoryDetailDTO;
    }


}

