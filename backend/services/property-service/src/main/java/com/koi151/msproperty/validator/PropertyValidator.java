package com.koi151.msproperty.validator;

import com.koi151.msproperty.customExceptions.InvalidRequestException;
import com.koi151.msproperty.model.request.property.PropertyCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class PropertyValidator {
    public void checkValidPropertyCreateRequest(PropertyCreateRequest request) {
        if (request.propertyForRent() != null && request.propertyForSale() != null)
            throw new InvalidRequestException("Property post cannot set for both sale and rent, choose one");
    }

}
