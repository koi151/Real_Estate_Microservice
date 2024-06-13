package com.koi151.msproperties.service.imp;

import com.koi151.msproperties.dto.FullPropertyDTO;
import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.StatusEnum;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import customExceptions.PropertyNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PropertiesService {

    List<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params);
    PropertyEntity getPropertyById(Integer id);
    List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId);
    List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status);
    FullPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> images);
    FullPropertyDTO updateProperty(Integer id, PropertyUpdateRequest request, List<MultipartFile> imageFiles);
    void deleteProperty(Integer id) throws PropertyNotFoundException;
}
