package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.entity.payload.request.PropertyRequest;

public interface PropertiesServiceImp {

    boolean createProperty(PropertyRequest propertyRequest);
}
