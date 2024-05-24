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

    @GetMapping("/")
    public String getCategories () {
        return "get categories";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory (@RequestBody PropertyCategoryRequest propertyCategoryRequest) {
        ResponseData responseData = new ResponseData();

        System.out.println("data: "+ System.getenv("DATABASE_URL"));

        try {
            responseData.setData(propertyCategoryImp.createCategory(propertyCategoryRequest));

            responseData.setStatus(200);
            responseData.setDesc("Success");

            return new ResponseEntity<>(responseData, HttpStatus.OK);

        } catch (Exception e) { // temporary error handle
            responseData.setStatus(400);
            responseData.setDesc("Failed to create category");
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

    }


}
