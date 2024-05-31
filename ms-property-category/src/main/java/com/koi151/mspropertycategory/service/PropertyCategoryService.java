package com.koi151.mspropertycategory.service;

import com.koi151.mspropertycategory.client.PropertiesClient;
import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import customExceptions.CategoryNotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyCategoryService implements PropertyCategoryImp {

    @Autowired
    PropertyCategoryRepository propertyCategoryRepository;

    @Autowired
    PropertiesClient propertiesClient;

    @Autowired
    private CloudinaryService cloudinaryService;

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
    public List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategory> categories = propertyCategoryRepository.findByStatusEnum(status, pageRequest);

        return categories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    @Override
    public FullCategoryResponse findCategoryWithProperties(Integer categoryId) {
        var category = propertyCategoryRepository.findById(categoryId)
                .orElse(
                        PropertyCategory.builder()
                                .title("NOT FOUND")
                                .imageUrls("NOT FOUND")
                                .description("NOT FOUND")
                                .build()
                );


        List<Properties> properties = propertiesClient.findAllPropertiesByCategory(categoryId);

        return FullCategoryResponse.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .statusEnum(category.getStatusEnum())
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
            propertyCategoryDTO.setImages(category.getImageUrls());

            propertyCategoryDTOList.add((propertyCategoryDTO));
        }

        return propertyCategoryDTOList;
    }

    public PropertyCategory createCategory(PropertyCategoryRequest request) { // throws CloudinaryUploadException

        PropertyCategory propertyCategory = new PropertyCategory();

        propertyCategory.setTitle(request.getTitle());
        propertyCategory.setDescription(request.getDescription());
        propertyCategory.setStatusEnum(request.getStatusEnum());
        propertyCategory.setDeleted(false);
        propertyCategory.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String imageUrls = cloudinaryService.uploadFile(request.getImages(), "real_estate_categories");
            if (imageUrls == null || imageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload image to Cloudinary");
            }
            propertyCategory.setImageUrls(imageUrls);
        }

        propertyCategoryRepository.save(propertyCategory);

        return propertyCategory;
    }


    @Override
    public PropertyCategoryDetailDTO
    updateCategory(Integer id, PropertyCategoryRequest categoryRequest)
            throws CategoryNotFoundException
    {
        return propertyCategoryRepository.findById(id)
                .map(existingCategory -> {

                    if (categoryRequest.getTitle() != null)
                        existingCategory.setTitle(categoryRequest.getTitle());
                    if (categoryRequest.getDescription() != null)
                        existingCategory.setDescription(categoryRequest.getDescription());
                    if (categoryRequest.getStatusEnum() != null)
                        existingCategory.setStatusEnum(categoryRequest.getStatusEnum());

                    if (categoryRequest.getImages() != null) {
                        String imageUrls = cloudinaryService.uploadFile(categoryRequest.getImages(), "real_estate_categories");
                        if (imageUrls == null || imageUrls.isEmpty()) {
                            throw new RuntimeException("Failed to upload image to Cloudinary");
                        }
                        existingCategory.setImageUrls(imageUrls);
                    }

                    existingCategory.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

                    return propertyCategoryRepository.save(existingCategory);
                })
                .map(PropertyCategoryDetailDTO::new) // Map to DTO after saving
                .orElseThrow(() -> new CategoryNotFoundException("Property category not found with id: " + id));
    }

    @Override
    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        propertyCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setDeleted(true);
                    return propertyCategoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
}
