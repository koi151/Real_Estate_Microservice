package com.koi151.property_submissions.validator;

import com.koi151.property_submissions.client.PropertyClient;
import com.koi151.property_submissions.customExceptions.*;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.response.ResponseData;
import com.koi151.property_submissions.repository.PropertySubmissionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertySubmissionValidator {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertyClient propertyClient;

    public void validatePropertySubmissionCreateRequest(PropertySubmissionCreate request) {
       try {
           ResponseEntity<ResponseData> propertyResponse = propertyClient.propertyActiveCheck(request.propertyId());
           // check property existence by id
           if (propertyResponse.getStatusCode().is2xxSuccessful() && propertyResponse.getBody() != null) {
               if (propertyResponse.getBody().getData().equals(false))
                   throw new EntityNotFoundException("Property not found with id: " + request.propertyId());
           } else {
               throw new PropertyServiceResponseException("Failed to check property existence");
           }

           if (propertySubmissionRepository.existsByPropertyIdAndDeleted(request.propertyId(), false)) {
               throw new DuplicatePropertySubmissionException("Duplicate property submission");
           }

           if (propertySubmissionRepository.existsByReferenceCode(request.referenceCode())) {
               throw new DuplicateReferenceCodeException("Duplicate reference code");
           }

       } catch (FeignException ex) {
           System.out.println("Error fetching account information: {}" + ex.getMessage());
           throw new ProductServiceCommunicationException("Error communicating with account service");
       }
    }
}
