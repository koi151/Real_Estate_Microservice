package com.koi151.property_submissions.service;

import com.koi151.property_submissions.model.dto.AccountWithSubmissionDTO;
import com.koi151.property_submissions.model.dto.PropertySubmissionCreateDTO;
import com.koi151.property_submissions.model.dto.PropertySubmissionDetailedDTO;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertySubmissionService {
    Page<PropertySubmissionDetailedDTO> findAllPropertySubmissions(PropertySubmissionSearchRequest request, Pageable pageable);
    PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request);
    AccountWithSubmissionDTO getPropertySubmissionByAccount(Long accountId, Pageable pageable);
}
