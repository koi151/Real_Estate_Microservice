package com.koi151.msproperties.service;

import com.koi151.msproperties.model.dto.PropertyPostServiceDTO;

public interface PropertyPostService {
    PropertyPostServiceDTO findPostServicesById(Long propertyId);
}
