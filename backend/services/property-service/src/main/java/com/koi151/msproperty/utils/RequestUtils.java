package com.koi151.msproperty.utils;

import com.koi151.msproperty.model.request.property.PropertyFilterRequest;

public class RequestUtils {

    public static boolean locationRequested(PropertyFilterRequest request) {
        var addressRequest = request.address();
        return addressRequest != null && (StringUtils.checkString(addressRequest.city())
                || StringUtils.checkString(addressRequest.district())
                || StringUtils.checkString(addressRequest.ward())
                || StringUtils.checkString(addressRequest.streetAddress()));
    }
}
