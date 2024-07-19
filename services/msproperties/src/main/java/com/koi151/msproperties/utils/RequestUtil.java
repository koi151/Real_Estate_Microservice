package com.koi151.msproperties.utils;

import com.koi151.msproperties.model.request.property.PropertySearchRequest;

public class RequestUtil {

    public static boolean roomRequested(PropertySearchRequest request) {
        return request.bedrooms() != null || request.bathrooms() != null || request.kitchens() != null;
    }

    public static boolean locationRequested(PropertySearchRequest request) {
        var addressRequest = request.addressSearchRequest();
        return StringUtil.checkString(addressRequest.city())
                || StringUtil.checkString(addressRequest.district())
                || StringUtil.checkString(addressRequest.ward())
                || StringUtil.checkString(addressRequest.streetAddress());
    }
}
