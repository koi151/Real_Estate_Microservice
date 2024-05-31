package com.koi151.mspropertycategory.controller;

import com.koi151.mspropertycategory.entity.payload.FullCategoryResponse;
import com.koi151.mspropertycategory.entity.payload.ResponseData;
import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import com.koi151.mspropertycategory.service.imp.PropertyCategoryImp;
import com.koi151.mspropertycategory.validate.PropertyCategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/property-category")
public class PropertyCategoryController {

    @Autowired
    PropertyCategoryImp propertyCategoryImp;

//    PropertyCategoryValidator propertyCategoryValidator;

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

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getCategoriesByStatus(@PathVariable(name = "status") String status) {
        ResponseData responseData = new ResponseData();

//        responseData.setData(propertyCategoryImp.);

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createCategory(@ModelAttribute PropertyCategoryRequest request) {
        ResponseData responseData = new ResponseData();

        PropertyCategoryValidator.validateCategoryRequest(request);
        responseData.setData(propertyCategoryImp.createCategory(request));

        responseData.setStatus(200);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> updateCategory(
            @PathVariable(name = "id") Integer id,
            @ModelAttribute PropertyCategoryRequest request
    ){

        ResponseData responseData = new ResponseData();

//        PropertyCategoryValidator.validateCategoryRequest(request);

        responseData.setData(propertyCategoryImp.updateCategory(id, request));
        responseData.setDesc("Updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteCategory(@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        propertyCategoryImp.deleteCategory(id);
        responseData.setDesc("Deleted successful");

        return ResponseEntity.ok(responseData);
    }
}
