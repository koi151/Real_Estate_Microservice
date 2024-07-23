package com.koi151.property_submissions.repository.custom;

import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPropertySubmissionRepository {
    Page<PropertySubmission> findPropertySubmissionsByCriteria(PropertySubmissionSearchRequest request, Pageable pageable);
}
