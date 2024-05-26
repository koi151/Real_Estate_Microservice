package com.koi151.msproperties.controller;

import com.koi151.msproperties.entity.payload.ResponseData;
import com.koi151.msproperties.entity.payload.request.PropertyRequest;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertiesController {

    @Autowired
    PropertiesServiceImp propertiesServiceImp;

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

    @GetMapping("/")
    public String getProperties () {
        return "Get properties";
    }

}
