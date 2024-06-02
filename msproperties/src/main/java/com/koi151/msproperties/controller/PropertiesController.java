package com.koi151.msproperties.controller;

import com.koi151.msproperties.dto.FullPropertiesDTO;
import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.StatusEnum;
import com.koi151.msproperties.entity.payload.ResponseData;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertiesController {

    @Autowired
    PropertiesServiceImp propertiesServiceImp;

    @GetMapping("/home-properties")
    public ResponseEntity<?> getHomeProperties() {
        List<PropertiesHomeDTO> properties = propertiesServiceImp.getHomeProperties();

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No property found"
                : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData> getPropertyById(@PathVariable(name = "id") Integer id) {
        Properties properties = propertiesServiceImp.getPropertyById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<ResponseData> getPropertiesByCategory(@PathVariable(name="category-id") Integer categoryId) {
        List<PropertiesHomeDTO> properties = propertiesServiceImp.findAllPropertiesByCategory(categoryId);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No properties found with category id: " + categoryId : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getPropertiesWithStatus(@PathVariable(name = "status") String status) {
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());
        List<PropertiesHomeDTO> properties = propertiesServiceImp.getPropertiesWithStatus(se);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No properties found with status " + status
                : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createProperty(@ModelAttribute @Valid PropertyCreateRequest propertyCreateRequest) {
        FullPropertiesDTO properties = propertiesServiceImp.createProperty(propertyCreateRequest);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc("Property created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
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
        ResponseData responseData = new ResponseData();

        responseData.setData(propertiesServiceImp.updateProperty(id, request));
        responseData.setDesc("Property updated successfully");

        return ResponseEntity.ok(responseData);
    }

}
