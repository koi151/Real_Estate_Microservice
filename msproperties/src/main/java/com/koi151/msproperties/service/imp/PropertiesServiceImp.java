package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.payload.request.PropertyRequest;

import java.util.List;
import java.util.Properties;

public interface PropertiesServiceImp {

    List<PropertiesHomeDTO> getHomeProperties();
    boolean createProperty(PropertyRequest propertyRequest);
    List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId);
}
