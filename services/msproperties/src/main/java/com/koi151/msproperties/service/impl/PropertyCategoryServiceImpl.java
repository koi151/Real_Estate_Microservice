package com.koi151.msproperties.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperties.customExceptions.CategoryNotFoundException;
import com.koi151.msproperties.customExceptions.MaxImagesExceededException;
import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.mapper.PropertyCategoryMapper;
import com.koi151.msproperties.model.dto.PropertiesHomeDTO;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryDetailDTO;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryTitleDTO;
import com.koi151.msproperties.model.reponse.PropertyCategorySearchResponse;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import com.koi151.msproperties.repository.PropertyCategoryRepository;
import com.koi151.msproperties.service.PropertyCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class PropertyCategoryServiceImpl implements PropertyCategoryService {

    private final PropertyCategoryRepository propertyCategoryRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final PropertyCategoryMapper propertyCategoryMapper;

    @Override
    public Page<PropertyCategoryHomeDTO> getCategoriesHomePage(Pageable pageable) {
        Page<PropertyCategoryEntity> categoryEntities = propertyCategoryRepository.findAll(pageable);
        return categoryEntities.map(propertyCategoryMapper::toPropertyCategoryHomeDTO);
    }

    @Override
    public List<PropertyCategoryHomeDTO> getCategoriesByTitle(String title) {
        List<PropertyCategoryEntity> propertyCategories = propertyCategoryRepository.findByTitleContainingIgnoreCase(title);

        return propertyCategories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyCategoryHomeDTO> getCategoriesByStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("categoryId"));
        Page<PropertyCategoryEntity> categories = propertyCategoryRepository.findByStatus(status, pageRequest);

        return categories.stream()
                .map(category -> new PropertyCategoryHomeDTO(category.getTitle(), category.getDescription(), category.getImageUrls()))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyCategoryEntity getCategoryById(Integer id) {
        return propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
    }

//    @Override
//    public FullCategoryResponse findCategoryWithProperties(Integer categoryId) {
//        var category = propertyCategoryRepository.findById(categoryId)
//                .orElseThrow(() -> new CategoryNotFoundException("No category found with id " + categoryId));
//
//        ResponseEntity<ResponseData> responseEntity = propertiesClient.findAllPropertiesByCategory(categoryId);
//        ResponseData responseData = Objects.requireNonNull(responseEntity.getBody());
//
//        List<PropertyEntity> properties;
//        try { // convert object to List<Properties>
//            properties = objectMapper.convertValue(responseData.getData(), new TypeReference<>() {});
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to deserialize properties data", e);
//        }
//
//        return FullCategoryResponse.builder()
//                .title(category.getTitle())
//                .description(category.getDescription())
//                .statusEnum(category.getStatus())
//                .properties(properties)
//                .build();
//    }

    @Override
    public PropertyCategoryTitleDTO getCategoryTitleById(Integer id){
        PropertyCategoryEntity category = propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new CategoryNotFoundException("No property category found with id " + id));
        return new PropertyCategoryTitleDTO(category.getTitle());
    }


//    @Override
//    public List<PropertyCategorySearchResponse> findAllPropertyCategories(PropertyCategorySearchRequest request) {
//        List<PropertyCategoryEntity> propertyCategories = propertyCategoryRepository.getPropertyCategoryByCriteria(request);
//        List<PropertyCategorySearchResponse> result = new ArrayList<>();
//
//        for (PropertyCategoryEntity item : propertyCategories) {
//            PropertyCategorySearchResponse category =  propertyCategoryConverter.toPropertyCategorySearchResponse(item);
//            result.add(category);
//        }
//
//        return result;
//    }

    public PropertyCategoryEntity createCategory(PropertyCategoryCreateRequest request, List<MultipartFile> imageFiles) { // throws CloudinaryUploadException

        PropertyCategoryEntity propertyCategoryEntity = PropertyCategoryEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_categories");
            if (imageUrls == null || imageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload images to Cloudinary");
            }
            propertyCategoryEntity.setImageUrls(imageUrls);
        }

        propertyCategoryRepository.save(propertyCategoryEntity);

        return propertyCategoryEntity;
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

    private void updateImages(PropertyCategoryEntity existingProperty, PropertyCategoryUpdateRequest request, List<MultipartFile> imageFiles) {
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

    private void updateCategoryDetails(PropertyCategoryEntity existingCategory, PropertyCategoryUpdateRequest request) {
        if (request != null) {

            // Use Optional for Null check
            Optional.ofNullable(request.getTitle()).ifPresent(existingCategory::setTitle);
            Optional.ofNullable(request.getDescription()).ifPresent(existingCategory::setDescription);
            Optional.ofNullable(request.getStatus()).ifPresent(existingCategory::setStatus);
        }
    }

    private PropertyCategoryDetailDTO convertToPropertyCategoryDTO(PropertyCategoryEntity savedProperty, PropertyCategoryUpdateRequest request) {

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
