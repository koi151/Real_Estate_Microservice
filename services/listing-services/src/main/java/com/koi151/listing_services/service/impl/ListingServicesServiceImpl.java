package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.client.PropertyClient;
import com.koi151.listing_services.customExceptions.PostServiceNotExistedException;
import com.koi151.listing_services.customExceptions.PropertyNotFoundException;
import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.mapper.PropertyServicePackageMapper;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.repository.PropertyPostPackageRepository;
import com.koi151.listing_services.service.ListingServicesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingServicesServiceImpl implements ListingServicesService {

    private final PropertyServicePackageMapper propertyServicePackageMapper;
    private final PropertyPostPackageRepository propertyPostPackageRepository;
    private final PostServiceRepository postServiceRepository;
    private final PropertyClient propertyClient;

    private void postServiceCreateValidate(PropertyServicePackageCreateRequest request) {
//        boolean propertyServicePackageExisted = propertyPostPackageRepository.fi

        // property id check
        var propertyResponse = propertyClient.propertyExistsCheck(request.propertyId());
        if (!propertyResponse.getStatusCode().is2xxSuccessful() || propertyResponse.getBody() == null) //
            throw new PropertyNotFoundException("Property not found with id: " + request.propertyId());

        // post service ids check
        Set<Long> propertyPostIdsNotExists = postServiceRepository.findMissingPostServiceIds(request.postServicePackageIds());
        if (!propertyPostIdsNotExists.isEmpty())
            throw new PostServiceNotExistedException("Property service ids not exists: " + propertyPostIdsNotExists);

    }

    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPostServicePackage(PropertyServicePackageCreateRequest request) {
        postServiceCreateValidate(request);
        PropertyServicePackage postServicePackage = propertyServicePackageMapper.toPostServicePackageEntity(request);
        propertyPostPackageRepository.save(postServicePackage);
        return propertyServicePackageMapper.toPropertyServicePackageCreateDTO(postServicePackage);
    }
}






