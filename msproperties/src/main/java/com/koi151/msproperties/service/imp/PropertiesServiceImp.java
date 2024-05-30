package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import customExceptions.PropertyNotFoundException;

import java.util.List;

public interface PropertiesServiceImp {

    List<PropertiesHomeDTO> getHomeProperties();
    boolean createProperty(PropertyCreateRequest propertyCreateRequest);
    List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId);
    Properties updateProperty(Integer id, PropertyUpdateRequest request);
    void deleteProperty(Integer id) throws PropertyNotFoundException;
}
