package com.koi151.ms_post_approval.validation;

import com.koi151.ms_post_approval.customExceptions.DuplicatePropertySubmissionException;
import com.koi151.ms_post_approval.customExceptions.DuplicateReferenceCodeException;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.repository.PropertySubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertySubmissionValidator {

    private final PropertySubmissionRepository propertySubmissionRepository;
    public void validatePropertySubmissionCreateRequest(PropertySubmissionCreate request) {
        if (propertySubmissionRepository.existsByPropertyIdAndDeleted(request.propertyId(), false)) {
            throw new DuplicatePropertySubmissionException("Duplicate property submission");
        }

        if (propertySubmissionRepository.existsByReferenceCode(request.referenceCode())) {
            throw new DuplicateReferenceCodeException("Duplicate reference code");
        }
    }
}
