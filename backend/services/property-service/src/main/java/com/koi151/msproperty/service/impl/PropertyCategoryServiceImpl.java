package com.koi151.msproperty.service.impl;

import com.koi151.msproperty.customExceptions.CategoryNotFoundException;
import com.koi151.msproperty.customExceptions.MaxImagesExceededException;
import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.mapper.PropertyCategoryMapper;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryDetailDTO;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryTitleDTO;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import com.koi151.msproperty.repository.PropertyCategoryRepository;
import com.koi151.msproperty.service.PropertyCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyCategoryServiceImpl implements PropertyCategoryService {

    private final PropertyCategoryRepository propertyCategoryRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final PropertyCategoryMapper propertyCategoryMapper;

    @Override
    public Page<PropertyCategoryHomeDTO> getCategoriesHomePage(PropertyCategorySearchRequest request,Pageable pageable) {
        Page<PropertyCategory> categoryEntities = propertyCategoryRepository.getPropertyCategoryByCriteria(request, pageable);
        return categoryEntities.map(propertyCategoryMapper::toPropertyCategoryHomeDTO);
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
    public PropertyCategoryTitleDTO getCategoryTitleById(Integer id){
        PropertyCategory category = propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
        return new PropertyCategoryTitleDTO(category.getTitle());
    }

    public PropertyCategory createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> imageFiles) { // throws CloudinaryUploadException

        PropertyCategory propertyCategory = PropertyCategory.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_categories");
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
        if (request != null && request.imageUrlsRemove() != null && !request.imageUrlsRemove().isEmpty()) {
            existingImagesUrlSet.removeAll(request.imageUrlsRemove());
        }

        // Handle image addition
        if (imageFiles != null && !imageFiles.isEmpty()) {
            int totalImagesAfterAddition = existingImagesUrlSet.size() + imageFiles.size();
            if (totalImagesAfterAddition > 8) {
                throw new MaxImagesExceededException("Cannot store more than 8 images. You are trying to add " + imageFiles.size() + " images to the existing " + existingImagesUrlSet.size() + " images.");
            }

            String newImageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_categories");
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
            Optional.ofNullable(request.title()).ifPresent(existingCategory::setTitle);
            Optional.ofNullable(request.description()).ifPresent(existingCategory::setDescription);
            Optional.ofNullable(request.status()).ifPresent(existingCategory::setStatus);
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
