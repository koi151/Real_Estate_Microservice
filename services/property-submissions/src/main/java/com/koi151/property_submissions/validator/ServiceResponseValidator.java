package com.koi151.property_submissions.validator;

import com.koi151.property_submissions.customExceptions.ResourceNotFoundException;
import com.koi151.property_submissions.model.response.ResponseData;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceResponseValidator {

    // Supplier is functional interface
    public ResponseEntity<ResponseData> fetchServiceData(Supplier<ResponseEntity<ResponseData>> serviceCall, String serviceName, String dataDescription) {
        try {
            ResponseEntity<ResponseData> response = serviceCall.get();
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Failed to fetch " + dataDescription + " from " + serviceName + " service");
            }
            return response;

        } catch (FeignException ex) {
//            log.error("Error occurred while fetching data in {} service: {}", serviceName, ex.getMessage().startsWith("details:[\""));
            String errorMessage = ex.getMessage();
            int detailsPos = ex.getMessage().indexOf("details");
            errorMessage = errorMessage.substring(detailsPos+11, errorMessage.length() - 4);
            throw new ResourceNotFoundException(errorMessage);
        }
    }
}
