package com.koi151.msproperties.utils;

import com.koi151.msproperties.model.request.property.PropertySearchRequest;

public class RequestUtil {

    public static boolean locationRequested(PropertySearchRequest request) {
        var addressRequest = request.address();
        return addressRequest != null && (StringUtil.checkString(addressRequest.city())
                || StringUtil.checkString(addressRequest.district())
                || StringUtil.checkString(addressRequest.ward())
                || StringUtil.checkString(addressRequest.streetAddress()));
    }
}
