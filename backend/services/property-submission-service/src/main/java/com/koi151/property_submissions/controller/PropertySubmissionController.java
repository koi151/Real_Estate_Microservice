package com.koi151.property_submissions.controller;

import com.koi151.property_submissions.mapper.PaginationContext;
import com.koi151.property_submissions.mapper.ResponseDataMapper;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import com.koi151.property_submissions.model.response.ResponseData;
import com.koi151.property_submissions.service.PropertySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
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
    private final ResponseDataMapper responseDataMapper;

    private static final int MAX_PAGE_SIZE = 20;

    @PostMapping("/")
    public ResponseEntity<ResponseData> createPropertySubmission(@RequestBody @Valid PropertySubmissionCreate request) {
        var submissions = propertySubmissionService.createPropertySubmission(request);

        return new ResponseEntity<>(
            ResponseData.builder()
                .data(submissions)
                .description("Property submission created successfully, will be reviewed by admin as soon as possible")
                .build(),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/")
    public ResponseEntity<ResponseData> findAllPropertySubmissions(
            @RequestBody @Valid PropertySubmissionSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(
                page - 1,
                Math.min(limit, MAX_PAGE_SIZE),
                Sort.by("createdDate").descending()
        );
        var submissionPage = propertySubmissionService.findAllPropertySubmissions(request, pageable);

        PaginationContext paginationContext = new PaginationContext(page, limit);
        ResponseData responseData = responseDataMapper.toResponseData(submissionPage, paginationContext);
        responseData.setDescription(!submissionPage.getContent().isEmpty()
                ? "Get properties succeed"
                : "No property matched the searching criteria"
        );
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/accountId/{account-id}")
    public ResponseEntity<ResponseData> getPropertySubmissionByAccount(
            @PathVariable(name = "account-id") Long accountId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());
        var accountWithSubmission = propertySubmissionService.getPropertySubmissionByAccount(accountId, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(accountWithSubmission);
        responseData.setDescription(accountWithSubmission.getPropertySubmissionsDetailsDTO().getContent().isEmpty()
                        ? "Account have no property post"
                        : String.format("Get property posts succeed by account id: %s", accountId));
        return ResponseEntity.ok(responseData);
    }
}














