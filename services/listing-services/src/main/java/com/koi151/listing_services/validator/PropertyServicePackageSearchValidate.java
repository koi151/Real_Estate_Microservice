package com.koi151.listing_services.validator;

import com.koi151.listing_services.customExceptions.ValidationFailedException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertyServicePackageSearchValidate {

    public void propertyServicePackageSearchValidate(Map<String, String> params) {
        if (params.get("packageId") == null && params.get("propertyId") == null)
            throw new ValidationFailedException("Property service package id or property id is mandatory for searching");
    }
}
