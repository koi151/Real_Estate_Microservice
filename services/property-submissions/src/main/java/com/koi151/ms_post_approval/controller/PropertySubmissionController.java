package com.koi151.ms_post_approval.controller;

import com.koi151.ms_post_approval.mapper.ResponseDataMapper;
import com.koi151.ms_post_approval.model.dto.AccountWithSubmissionDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.model.response.ResponseData;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property-submissions")
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
public class PropertySubmissionController {

    private final PropertySubmissionService propertySubmissionService;
//    private final ResponseDataMapper responseDataMapper;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertySubmission(@RequestBody @Valid PropertySubmissionCreate request) {
        var submissions = propertySubmissionService.createPropertySubmission(request);

        ResponseData responseData = ResponseData.builder()
                .data(submissions)
                .description("Property submission created successfully, will be reviewed by admin as soon as possible")
                .build();

        return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    @GetMapping("/accountId/{account-id}")
    public ResponseEntity<ResponseData> getPropertySubmissionByAccount(
            @PathVariable(name = "account-id") Long accountId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate"));
        var accountWithSubmission = propertySubmissionService.getPropertySubmissionByAccount(accountId, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(accountWithSubmission);
        responseData.setDescription(accountWithSubmission.getPropertySubmissionDTO().getContent().isEmpty()
                        ? "Account have no property post"
                        : String.format("Get property posts succeed by account id: %s", accountId));
        return ResponseEntity.ok(responseData);
    }


}














