package com.koi151.msproperties.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperties.dto.FullPropertyDTO;
import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.PropertyTypeEnum;
import com.koi151.msproperties.entity.StatusEnum;
import com.koi151.msproperties.entity.payload.ResponseData;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import com.koi151.msproperties.service.imp.PropertiesService;
import customExceptions.PaymentScheduleNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/properties")
@PropertySource("classpath:application.yml")
public class PropertyController {

    @Autowired
    PropertiesService propertiesService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/home-properties")
    public ResponseEntity<?> getHomeProperties(@RequestParam Map<String, Object> params) {
        List<PropertiesHomeDTO> properties = propertiesService.getHomeProperties(params);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No property found"
                : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData> getPropertyById(@PathVariable(name = "id") Integer id) {
        PropertyEntity propertyEntity = propertiesService.getPropertyById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertyEntity);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<ResponseData> getPropertiesByCategory(@PathVariable(name="category-id") Integer categoryId) {
        List<PropertiesHomeDTO> properties = propertiesService.findAllPropertiesByCategory(categoryId);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No properties found with id: " + categoryId : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> getPropertiesWithStatus(@PathVariable(name = "status") String status) {
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());
        List<PropertiesHomeDTO> properties = propertiesService.getPropertiesWithStatus(se);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No properties found with status " + status
                : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createProperty(
            @RequestPart @Valid PropertyCreateRequest property,
            @RequestPart(required = false) List<MultipartFile> images

    ) {

        if (property.getType() == PropertyTypeEnum.RENT && property.getPaymentSchedule() == null)
            throw new PaymentScheduleNotFoundException("Payment schedule required in property for sale");

        FullPropertyDTO propertyRes = propertiesService.createProperty(property, images);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertyRes);
        responseData.setDesc("Property created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Integer id) {
        ResponseData responseData = new ResponseData();

        propertiesService.deleteProperty(id);
        responseData.setDesc("Property deleted successfully");

        return ResponseEntity.ok(responseData);
    }


    @PatchMapping("/{id}")
    public  ResponseEntity<ResponseData> updateProperty(
            @PathVariable(name = "id") Integer id,
            @RequestPart(required = false) @Valid PropertyUpdateRequest property,
            @RequestPart(required = false) List<MultipartFile> images
    ){
        ResponseData responseData = new ResponseData();

        responseData.setData(propertiesService.updateProperty(id, property, images));
        responseData.setDesc("Property updated successfully");

        return ResponseEntity.ok(responseData);
    }

}