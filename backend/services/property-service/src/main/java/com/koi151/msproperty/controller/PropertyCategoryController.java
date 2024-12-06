package com.koi151.msproperty.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperty.enums.CategoryStatusEnum;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.mapper.ResponseDataMapper;
import com.koi151.msproperty.model.request.property.PropertySearchRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryStatusUpdateRequest;
import com.koi151.msproperty.model.response.ResponseData;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import com.koi151.msproperty.service.PropertyCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties/categories")
public class PropertyCategoryController {

    private final PropertyCategoryService propertyCategoryService;
    private final ResponseDataMapper responseDataMapper;
    private static final int MAX_PAGE_SIZE = 20;

    @GetMapping("")
//    @PreAuthorize("hasAuthority('SCOPE_property_category_view')")
    public ResponseEntity<ResponseData> getCategoriesHomePage(
        @RequestParam(required = false, defaultValue = "1")
        @Min(value = 1, message = "Page number must be at least 1") int page,
        @RequestParam(required = false, defaultValue = "10")
        @Min(value = 1, message = "Page size must be at least 1") int limit,
        @ModelAttribute @Valid PropertyCategorySearchRequest request
    ) {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());

        var categoriesPage = propertyCategoryService.getCategoriesHomePage(request, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(categoriesPage, page, limit);
        responseData.setDesc(categoriesPage.isEmpty() ?
                "No property category found" : "Get home property categories succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/title/{id}")
    public ResponseEntity<ResponseData> getCategoryTitleById(@PathVariable(name = "id") int id) {
        var category = propertyCategoryService.getCategoryTitleById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(category);
        responseData.setDesc("Get property category title by id succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
//    @PreAuthorize("hasAuthority('SCOPE_property_category_view')")
    public ResponseEntity<ResponseData> getCategoryById(@PathVariable(name = "id") Integer id) {
        var category = propertyCategoryService.getCategoryById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(category);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

//    @GetMapping("/with-properties/{category-id}")
//    public ResponseEntity<ResponseData> findCategoryWithProperties(@PathVariable(name = "category-id") Integer categoryId) {
//        FullCategoryResponse res = propertyCategory.findCategoryWithProperties(categoryId);
//
//        ResponseData responseData = new ResponseData();
//        responseData.setData(res);
//        responseData.setDesc(res.getProperties().isEmpty()
//                ? "No property found with category id " + categoryId
//                : "Success");
//
//        return ResponseEntity.ok(responseData);
//    }


    @GetMapping("/status/{status}")
//    @PreAuthorize("hasAuthority('SCOPE_property_category_edit')")
    public ResponseEntity<ResponseData> getCategoriesByStatus(@PathVariable(name = "status") String status) {
        ResponseData responseData = new ResponseData();
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());

        var properties = propertyCategoryService.getCategoriesByStatus(se);

        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No property category found with status " + status : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData> createCategory(
        @RequestPart("propertyCategory") String propertyCategoryJson,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        PropertyCategoryCreateRequest propertyCategory = objectMapper.readValue(propertyCategoryJson, PropertyCategoryCreateRequest.class);

        ResponseData response = new ResponseData();
        response.setData(propertyCategoryService.createCategory(propertyCategory, images));
        response.setDesc("Category created successfully!");

        return ResponseEntity.ok(response);
    }



    @GetMapping("/tree")
    public ResponseEntity<ResponseData> getCategoryTree() {
        var categoryTree = propertyCategoryService.getCategoryTree();
        return ResponseEntity.ok(ResponseData.builder()
            .data(categoryTree)
            .desc("Get category tree successful")
            .build());
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('SCOPE_property_category_update')")
    public ResponseEntity<ResponseData> updateCategory(
            @PathVariable(name = "id") Integer id,
            @RequestPart @Valid PropertyCategoryUpdateRequest category,
            @RequestPart List<MultipartFile> images
    ){
        var categoryRes = propertyCategoryService.updateCategory(id, category, images);

        ResponseData responseData = new ResponseData();
        responseData.setData(categoryRes);
        responseData.setDesc("Updated successfully");

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseData> updateCategoryStatus(
        @PathVariable Long id,
        @RequestBody @Valid PropertyCategoryStatusUpdateRequest request
    ){
        propertyCategoryService.updateCategoryStatus(id, request);
        return ResponseEntity.ok(ResponseData.builder()
            .desc("Property category updated status successfully")
            .build()
        );
    }


    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('SCOPE_property_category_delete')")
    public ResponseEntity<ResponseData> deleteCategory(@PathVariable(name = "id") Long id) {
        ResponseData responseData = new ResponseData();

        propertyCategoryService.deleteCategory(id);
        responseData.setDesc("Deleted property with id " + id);

        return ResponseEntity.ok(responseData);
    }
}
