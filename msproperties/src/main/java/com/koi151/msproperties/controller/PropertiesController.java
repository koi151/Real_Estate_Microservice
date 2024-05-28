package com.koi151.msproperties.controller;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.payload.ResponseData;
import com.koi151.msproperties.entity.payload.request.PropertyRequest;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createProperty(@RequestBody PropertyRequest propertyRequest) {
        ResponseData responseData = new ResponseData();

        try {
            responseData.setData(propertiesServiceImp.createProperty(propertyRequest));
            responseData.setDesc("Success");
            return ResponseEntity.ok(responseData);

        } catch (Exception e) { // temporary err handle
            responseData.setStatus(400);
            responseData.setDesc("Failed to create category");
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        propertiesServiceImp.deleteProperty(id);
        responseData.setDesc("Property deleted successfully");

        return ResponseEntity.ok(responseData);

    }

}
