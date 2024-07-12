package com.koi151.msproperties.controller;

import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.reponse.PropertyCategorySearchResponse;
import com.koi151.msproperties.model.reponse.ResponseData;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryCreateRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategoryUpdateRequest;
import com.koi151.msproperties.service.PropertyCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@PropertySource("classpath:application.yml")
@RequestMapping("/api/v1/property-category")
public class PropertyCategoryController {

    @Autowired
    PropertyCategoryService propertyCategoryService;

    @GetMapping("/")
    public ResponseEntity<ResponseData> propertyCategoriesList(@RequestBody(required = false) @Valid PropertyCategorySearchRequest request) {
        List<PropertyCategorySearchResponse> result = propertyCategoryService.findAllPropertyCategories(request);

        ResponseData responseData = new ResponseData();
        responseData.setData(result);
        responseData.setDesc(result.isEmpty() ? "No property category found" : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{title}")
    public ResponseEntity<ResponseData> getCategories(@PathVariable(name = "title") String title) {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryService.getCategoriesByTitle(title));

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/title/{id}")
    public ResponseEntity<ResponseData> getCategoryTitleById(@PathVariable(name = "id") int id) {
        var category = propertyCategoryService.getCategoryTitleById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(category);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/home-categories")
    public ResponseEntity<ResponseData> getCategoriesHomePage() {
        var properties = propertyCategoryService.getCategoriesHomePage();

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No property category found" : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
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
    public ResponseEntity<ResponseData> getCategoriesByStatus(@PathVariable(name = "status") String status) {
        ResponseData responseData = new ResponseData();
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());

        var properties = propertyCategoryService.getCategoriesByStatus(se);

        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No property category found with status " + status : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createCategory(
            @RequestPart @Valid PropertyCategoryCreateRequest propertyCategory,
            @RequestPart(required = false) List<MultipartFile> images
    ){
        ResponseData responseData = new ResponseData();

        responseData.setData(propertyCategoryService.createCategory(propertyCategory, images));
        responseData.setStatus(200);
        responseData.setDesc("Success");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @PatchMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteCategory(@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        propertyCategoryService.deleteCategory(id);
        responseData.setDesc("Deleted property with id " + id);

        return ResponseEntity.ok(responseData);
    }
}
