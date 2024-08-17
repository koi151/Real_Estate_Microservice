package com.koi151.listing_services.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.listing_services.client.PropertyClient;
import com.koi151.listing_services.customExceptions.DuplicatedPropertyPostPackageException;
import com.koi151.listing_services.customExceptions.PostServiceNotExistedException;
import com.koi151.listing_services.customExceptions.PropertyNotFoundException;
import com.koi151.listing_services.customExceptions.ServiceUnavailableException;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.repository.PropertyPostPackageRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class PostServiceValidate {

    private final PostServiceRepository postServiceRepository;
    private final PropertyPostPackageRepository propertyPostPackageRepository;
    private final PropertyClient propertyClient;
    private final ObjectMapper objectMapper;

    public void postServiceCreateValidate(PropertyServicePackageCreateRequest request) {
        // check whether property already have post package or not
        if (propertyPostPackageRepository.existsByPropertyId(request.propertyId()))
            throw new DuplicatedPropertyPostPackageException("Property with id: " + request.propertyId()
                    + " already has an active post service package");

        // property id check
        try {
            var propertyResponse = propertyClient.propertyExistsCheck(request.propertyId());
            if (!propertyResponse.getStatusCode().is2xxSuccessful() || propertyResponse.getBody() != null) {
                var propertyData = objectMapper.convertValue(
                        Objects.requireNonNull(Objects.requireNonNull(propertyResponse.getBody()).getData()), Boolean.class);

                if (!propertyData)
                    throw new PropertyNotFoundException("Property with id: " + request.propertyId() + " is not found or currently inactive");
            } else {
                throw new ServiceUnavailableException("Property service is unavailable right now");
            }
        } catch (FeignException ex) {
            throw new ServiceUnavailableException("Property service is unavailable right now");
        }

        // post service ids check
        List<Long> propertyPostIdsNotExists = postServiceRepository.findMissingPostServiceIds(request.postServiceIds());
        if (!propertyPostIdsNotExists.isEmpty())
            throw new PostServiceNotExistedException("Property service ids not exists: " + propertyPostIdsNotExists);
    }
}

