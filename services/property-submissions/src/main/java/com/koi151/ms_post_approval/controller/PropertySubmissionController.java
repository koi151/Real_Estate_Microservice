package com.koi151.ms_post_approval.controller;

import com.koi151.ms_post_approval.mapper.ResponseDataMapper;
import com.koi151.ms_post_approval.model.dto.AccountSubmissionDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.model.response.ResponseData;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property-submissions")
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class PropertySubmissionController {

    private final PropertySubmissionService propertySubmissionService;
    private final ResponseDataMapper responseDataMapper;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertySubmission(@RequestBody @Valid PropertySubmissionCreate request) {
        var propertySub = propertySubmissionService.createPropertySubmission(request);

        ResponseData responseData = ResponseData.builder()
            .data(propertySub)
            .description("Property submission created successful").build();
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/accountId/{account-id}")
    public ResponseEntity<ResponseData> getPropertySubmissionByAccount(
            @PathVariable(name = "account-id") Long accountId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate"));
        var propertyPages = propertySubmissionService.getPropertySubmissionByAccount(accountId, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(propertyPages,page, limit);
        responseData.setDescription(propertyPages.isEmpty()
                        ? "Account have no property post"
                        : String.format("Get property posts succeed by account id: %s", accountId));

        return ResponseEntity.ok(responseData);
    }

}














