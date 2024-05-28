package com.koi151.mspropertycategory.controller;

import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.ResponseData;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import com.koi151.mspropertycategory.validate.PropertyCategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/property-category")
public class PropertyCategoryController {

    @Autowired
    PropertyCategoryImp propertyCategoryImp;

    PropertyCategoryValidator propertyCategoryValidator;

    @GetMapping("/{title}")
    public ResponseEntity<ResponseData> getCategories(@PathVariable(name = "title") String title) {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategories(title));

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/title/{id}")
    public ResponseEntity<ResponseData> getCategoryTitleById(@PathVariable(name = "id") int id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategoryTitleById(id));
        responseData.setDesc("Success");
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/home-categories")
    public ResponseEntity<ResponseData> getCategoriesHomePage() {
        // tempo
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategoriesHomePage());
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/with-properties/{category-id}")
    public ResponseEntity<FullCategoryResponse> findCategoryWithProperties(@PathVariable(name = "category-id") Integer categoryId) {
        return ResponseEntity.ok(propertyCategoryImp.findCategoryWithProperties(categoryId));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createCategory (@RequestBody PropertyCategoryRequest propertyCategoryRequest) {
        ResponseData responseData = new ResponseData();

        try {
            PropertyCategoryValidator.validateCategoryRequest(propertyCategoryRequest);
            responseData.setData(propertyCategoryImp.createCategory(propertyCategoryRequest));

            responseData.setStatus(200);
            responseData.setDesc("Success");

            return ResponseEntity.ok(responseData);

        } catch (Exception e) { // temporary error handle
            responseData.setStatus(400);
            responseData.setDesc("Failed to create category");
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> updateCategory(@PathVariable(name = "id") Integer id, @RequestBody PropertyCategoryRequest categoryRequest) {
        ResponseData responseData = new ResponseData();

        try {
            PropertyCategoryValidator.validateCategoryRequest(categoryRequest);
            responseData.setData(propertyCategoryImp.updateCategory(id, categoryRequest));
            responseData.setDesc("Updated successfully");
            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            System.out.println("Error occurred while updating category: " + e.getMessage());
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteCategory(@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        try {
            propertyCategoryImp.deleteCategory(id);
            responseData.setDesc("Deleted successful");

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            System.out.println("Error occurred while deleting category: " + e.getMessage());
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

    }
}
