package com.koi151.msproperties.controller;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.payload.ResponseData;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertiesController {

    @Autowired
    PropertiesServiceImp propertiesServiceImp;

    @GetMapping("/home-properties")
    public ResponseEntity<?> getHomeProperties() {
        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesServiceImp.getHomeProperties());
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<List<PropertiesHomeDTO>> getPropertiesByCategory(@PathVariable(name="category-id") Integer categoryId) {
        List<PropertiesHomeDTO> propertiesList = propertiesServiceImp.findAllPropertiesByCategory(categoryId);
        return ResponseEntity.ok(propertiesList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProperty(@ModelAttribute @Valid PropertyCreateRequest propertyCreateRequest) {
        ResponseData responseData = new ResponseData();

        responseData.setData(propertiesServiceImp.createProperty(propertyCreateRequest));
        responseData.setDesc("Property created successful");

        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        propertiesServiceImp.deleteProperty(id);
        responseData.setDesc("Property deleted successfully");

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ResponseData> updateProperty(
            @PathVariable(name = "id") Integer id,
            @ModelAttribute @Valid PropertyUpdateRequest request
    ){
//        request.setPriceProvided(request.getPrice() != null);

        ResponseData responseData = new ResponseData();

        responseData.setData(propertiesServiceImp.updateProperty(id, request));
        responseData.setDesc("Property updated successfully");

        return ResponseEntity.ok(responseData);
    }

}
