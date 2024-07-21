package com.koi151.ms_post_approval.validation;

import com.koi151.ms_post_approval.client.PropertyClient;
import com.koi151.ms_post_approval.customExceptions.DuplicatePropertySubmissionException;
import com.koi151.ms_post_approval.customExceptions.DuplicateReferenceCodeException;
import com.koi151.ms_post_approval.customExceptions.PropertyNotFoundException;
import com.koi151.ms_post_approval.customExceptions.PropertyServiceResponseException;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.model.response.ResponseData;
import com.koi151.ms_post_approval.repository.PropertySubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertySubmissionValidator {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertyClient propertyClient;

    public void validatePropertySubmissionCreateRequest(PropertySubmissionCreate request) {
        ResponseEntity<ResponseData> propertyResponse = propertyClient.propertyExistsCheck(request.propertyId());
        // check property existence by id
        if (propertyResponse.getStatusCode().is2xxSuccessful() && propertyResponse.getBody() != null) {
            if (propertyResponse.getBody().getData().equals(false))
                throw new PropertyNotFoundException("Property not found with id: " + request.propertyId());
        } else {
            throw new PropertyServiceResponseException("Failed to check property existence");
        }

        if (propertySubmissionRepository.existsByPropertyIdAndDeleted(request.propertyId(), false)) {
            throw new DuplicatePropertySubmissionException("Duplicate property submission");
        }

        if (propertySubmissionRepository.existsByReferenceCode(request.referenceCode())) {
            throw new DuplicateReferenceCodeException("Duplicate reference code");
        }
    }
}
