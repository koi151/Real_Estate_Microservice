package com.koi151.ms_post_approval.repository.custom;

import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.model.request.PropertySubmissionSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPropertySubmissionRepository {
    Page<PropertySubmission> findPropertySubmissionsByCriteria(PropertySubmissionSearchRequest request, Pageable pageable);
}
