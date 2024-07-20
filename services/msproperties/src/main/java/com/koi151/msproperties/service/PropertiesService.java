package com.koi151.msproperties.service;

import com.koi151.msproperties.model.dto.DetailedPropertyDTO;
import com.koi151.msproperties.model.dto.PropertiesHomeDTO;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.PropertyPostServiceDTO;
import com.koi151.msproperties.model.dto.PropertySearchDTO;
import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperties.customExceptions.PropertyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PropertiesService {

    Page<PropertySearchDTO> findAllProperties(PropertySearchRequest request, Pageable pageable);
    Page<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params, Pageable pageable);
    DetailedPropertyDTO getPropertyById(Long id);
    Page<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId, Pageable pageable);
    Page<PropertySearchDTO> findAllPropertiesByAccount(Long accountId, Pageable pageable);
    Page<PropertiesHomeDTO> findPropertiesByStatus(StatusEnum status, Pageable pageable);
    DetailedPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> images);
    void createFakeProperties(List<PropertyCreateRequest> fakeProperties);
    DetailedPropertyDTO updateProperty(Long id, PropertyUpdateRequest request, List<MultipartFile> imageFiles);
    void deleteProperty(Long id) throws PropertyNotFoundException;
}
