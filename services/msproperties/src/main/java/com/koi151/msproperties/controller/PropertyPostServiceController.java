package com.koi151.msproperties.controller;

import com.koi151.msproperties.model.reponse.ResponseData;
import com.koi151.msproperties.service.PropertiesService;
import com.koi151.msproperties.service.PropertyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties/post-services")
public class PropertyPostServiceController {

    private final PropertyPostService propertyPostService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> findPostServicesById(@PathVariable(name = "id") Long id) {
        var postServiceData = propertyPostService.findPostServicesById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(postServiceData);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }
}
