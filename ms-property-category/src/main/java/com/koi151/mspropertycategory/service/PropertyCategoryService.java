package com.koi151.mspropertycategory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.koi151.mspropertycategory.client.PropertiesClient;
import com.koi151.mspropertycategory.dto.PropertyCategoryDetailDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryHomeDTO;
import com.koi151.mspropertycategory.dto.PropertyCategoryTitleDTO;
import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.ResponseData;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryCreateRequest;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryUpdateRequest;
import com.koi151.mspropertycategory.repository.PropertyCategoryRepository;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import customExceptions.CategoryNotFoundException;
import customExceptions.MaxImagesExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyCategoryService implements PropertyCategoryImp {

    @Autowired
    PropertyCategoryRepository propertyCategoryRepository;

    @Autowired
    PropertiesClient propertiesClient;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<PropertyCategoryHomeDTO> getCategories(String title) {
        List<PropertyCategory> propertyCategories = propertyCategoryRepository.findByTitleContainingIgnoreCase(title);

        return propertyCategories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategory> categories = propertyCategoryRepository.findByStatus(status, pageRequest);

        return categories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyCategory getCategoryById(Integer id) {
        return propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
    }

    @Override
    public FullCategoryResponse findCategoryWithProperties(Integer categoryId) {
        var category = propertyCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("No category found with id " + categoryId));

        ResponseEntity<ResponseData> responseEntity = propertiesClient.findAllPropertiesByCategory(categoryId);
        ResponseData responseData = Objects.requireNonNull(responseEntity.getBody());

        List<Properties> properties;
        try { // convert object to List<Properties>
            properties = objectMapper.convertValue(responseData.getData(), new TypeReference<List<Properties>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize properties data", e);
        }

        return FullCategoryResponse.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .statusEnum(category.getStatus())
                .properties(properties)
                .build();
    }

    @Override
    public PropertyCategoryTitleDTO getCategoryTitleById(Integer id){
        PropertyCategory category = propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
        return new PropertyCategoryTitleDTO(category.getTitle());
    }


    @Override
    public List<PropertyCategoryHomeDTO> getCategoriesHomePage() {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategory> categories = propertyCategoryRepository.findAll(pageRequest);

        return categories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    public PropertyCategory createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> imageFiles) { // throws CloudinaryUploadException

        PropertyCategory propertyCategory = PropertyCategory.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .updatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            String imageUrls = cloudinaryService.uploadFiles(imageFiles, "real_estate_categories");
            if (imageUrls == null || imageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload images to Cloudinary");
            }
            propertyCategory.setImageUrls(imageUrls);
        }

        propertyCategoryRepository.save(propertyCategory);

        return propertyCategory;
    }

    @Override
    public PropertyCategoryDetailDTO updateCategory(Integer id, PropertyCategoryUpdateRequest request, List<MultipartFile> imageFiles) {
        return propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .map(existingCategory -> {
                    updateCategoryDetails(existingCategory, request);
                    updateImages(existingCategory, request, imageFiles);

                    return propertyCategoryRepository.save(existingCategory);
                })
                .map(savedProperty -> convertToPropertyCategoryDTO(savedProperty, request))
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
    }

    private void updateImages(PropertyCategory existingProperty, PropertyCategoryUpdateRequest request, List<MultipartFile> imageFiles) {
        Set<String> existingImagesUrlSet = new HashSet<>();

        // Initialize existingImagesUrlSet with current image URLs if they exist
        if (existingProperty.getImageUrls() != null && !existingProperty.getImageUrls().isEmpty()) {
            existingImagesUrlSet = new HashSet<>(Arrays.asList(existingProperty.getImageUrls().split(",")));
        }

        // Handle image removal
        if (request != null && request.getImageUrlsRemove() != null && !request.getImageUrlsRemove().isEmpty()) {
            existingImagesUrlSet.removeAll(request.getImageUrlsRemove());
        }

        // Handle image addition
        if (imageFiles != null && !imageFiles.isEmpty()) {
            int totalImagesAfterAddition = existingImagesUrlSet.size() + imageFiles.size();
            if (totalImagesAfterAddition > 8) {
                throw new MaxImagesExceededException("Cannot store more than 8 images. You are trying to add " + imageFiles.size() + " images to the existing " + existingImagesUrlSet.size() + " images.");
            }

            String newImageUrls = cloudinaryService.uploadFiles(imageFiles, "real_estate_categories");
            if (newImageUrls == null || newImageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload images to Cloudinary");
            }
            existingImagesUrlSet.addAll(Arrays.asList(newImageUrls.split(",")));
        }

        // Convert updated set of image URLs back to a comma-separated string
        String updatedImageUrls = String.join(",", existingImagesUrlSet);
        existingProperty.setImageUrls(updatedImageUrls.isEmpty() ? null : updatedImageUrls);
    }

    private void updateCategoryDetails(PropertyCategory existingCategory, PropertyCategoryUpdateRequest request) {
        if (request != null) {

            // Use Optional for Null check
            Optional.ofNullable(request.getTitle()).ifPresent(existingCategory::setTitle);
            Optional.ofNullable(request.getDescription()).ifPresent(existingCategory::setDescription);
            Optional.ofNullable(request.getStatus()).ifPresent(existingCategory::setStatus);

            existingCategory.setUpdatedAt(LocalDateTime.now());
        }
    }

    private PropertyCategoryDetailDTO convertToPropertyCategoryDTO(PropertyCategory savedProperty, PropertyCategoryUpdateRequest request) {

        return PropertyCategoryDetailDTO.builder()
                .title(savedProperty.getTitle())
                .description(savedProperty.getDescription())
                .status(savedProperty.getStatus())
                .imageUrls(savedProperty.getImageUrls() != null && !savedProperty.getImageUrls().isEmpty()
                        ? Arrays.stream(savedProperty.getImageUrls().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    @Override
    public void deleteCategory(Integer id) {
        propertyCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setDeleted(true);
                    return propertyCategoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
}
