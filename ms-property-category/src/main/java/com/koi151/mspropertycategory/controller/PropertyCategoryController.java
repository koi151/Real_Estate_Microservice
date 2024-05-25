package com.koi151.mspropertycategory.controller;

import com.koi151.mspropertycategory.entity.payload.ResponseData;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property-category")
public class PropertyCategoryController {

    @Autowired
    PropertyCategoryImp propertyCategoryImp;

    @GetMapping("/{title}")
    public ResponseEntity<?> getCategories(@PathVariable(name = "title") String title) {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategories(title));

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/title/{id}")
    public ResponseEntity<?> getCategoryTitleById(@PathVariable(name = "id") int id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategoryTitleById(id));

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/home-categories")
    public ResponseEntity<?> getCategoriesHomePage() {
        // tempo
        ResponseData responseData = new ResponseData();
        responseData.setData(propertyCategoryImp.getCategoriesHomePage());
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory (@RequestBody PropertyCategoryRequest propertyCategoryRequest) {
        ResponseData responseData = new ResponseData();

        try {
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


}
