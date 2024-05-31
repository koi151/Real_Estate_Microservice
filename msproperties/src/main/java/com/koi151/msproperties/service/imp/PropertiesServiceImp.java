package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.StatusEnum;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import customExceptions.PropertyNotFoundException;

import java.util.List;

public interface PropertiesServiceImp {

    List<PropertiesHomeDTO> getHomeProperties();
    List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId);
    List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status);
    Properties createProperty(PropertyCreateRequest propertyCreateRequest);
    Properties updateProperty(Integer id, PropertyUpdateRequest request);
    void deleteProperty(Integer id) throws PropertyNotFoundException;
}
