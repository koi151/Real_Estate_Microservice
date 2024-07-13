package com.koi151.ms_post_approval.service;

import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;

public interface PropertySubmissionService {
    PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request);
}
