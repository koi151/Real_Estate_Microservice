package com.koi151.msproperty.service;

import com.koi151.msproperty.model.dto.DetailedPropertyDTO;
import com.koi151.msproperty.model.dto.PropertiesHomeDTO;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.dto.PropertySearchDTO;
import com.koi151.msproperty.model.request.property.PropertyCreateRequest;
import com.koi151.msproperty.model.request.property.PropertySearchRequest;
import com.koi151.msproperty.model.request.property.PropertyStatusUpdateRequest;
import com.koi151.msproperty.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperty.customExceptions.PropertyNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PropertiesService {

//    Page<PropertySearchDTO> findProperties(PropertySearchRequest request, Pageable pageable);
    Page<PropertySearchDTO> searchPropertiesForAdmin(PropertySearchRequest request, Pageable pageable);
    Page<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params, Pageable pageable);
    DetailedPropertyDTO getPropertyById(Long id);
    Page<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId, Pageable pageable);
    Page<PropertySearchDTO> findAllPropertiesByAccount(String accountId, Pageable pageable);
    Page<PropertiesHomeDTO> findPropertiesByStatus(StatusEnum status, Pageable pageable);
    void createProperty(PropertyCreateRequest request, List<MultipartFile> images);
    void createFakeProperties(List<PropertyCreateRequest> fakeProperties);
    DetailedPropertyDTO updateProperty(Long id, PropertyUpdateRequest request, List<MultipartFile> imageFiles);
    void deleteProperty(Long id) throws PropertyNotFoundException;
    void updatePropertyStatus(Long id, PropertyStatusUpdateRequest request);

    boolean propertyActiveCheck(Long id);
}
