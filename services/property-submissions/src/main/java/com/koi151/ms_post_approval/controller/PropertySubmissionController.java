package com.koi151.ms_post_approval.controller;

import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.model.response.ResponseData;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property-submissions")
@PropertySource("classpath:application.yml")
public class PropertySubmissionController {

    @Autowired
    PropertySubmissionService propertySubmissionService;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertySubmission(@RequestBody @Valid PropertySubmissionCreate request) {
        var propertySub = propertySubmissionService.createPropertySubmission(request);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertySub);
        responseData.setDescription("Property submission created successful");

        return ResponseEntity.ok(responseData);
    }
}














