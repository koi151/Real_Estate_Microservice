package com.koi151.listing_services.validator;

import com.koi151.listing_services.customExceptions.ValidationFailedException;
import com.koi151.listing_services.model.request.PropertyServicePackageSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class PropertyServicePackageSearchValidate {

    public void propertyServicePackageSearchValidate(PropertyServicePackageSearchRequest request) {
        if (request.packageId() == null && request.propertyId() == null)
            throw new ValidationFailedException("Property service package id or property id is mandatory for searching");
    }
}
