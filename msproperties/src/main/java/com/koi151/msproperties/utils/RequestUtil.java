package com.koi151.msproperties.utils;

import com.koi151.msproperties.model.request.PropertySearchRequest;

public class RequestUtil {

    public static boolean roomRequested(PropertySearchRequest request) {
        return request.getBedrooms() != null || request.getBathrooms() != null || request.getKitchens() != null;
    }

    public static boolean locationRequested(PropertySearchRequest request) {
        return StringUtil.checkString(request.getCity())
                || StringUtil.checkString(request.getDistrict())
                || StringUtil.checkString(request.getWard())
                || StringUtil.checkString(request.getStreet());
    }
}
