package com.koi151.listing_services.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.listing_services.client.PropertyClient;
import com.koi151.listing_services.customExceptions.DuplicatedPropertyPostPackageException;
import com.koi151.listing_services.customExceptions.PostServiceNotExistedException;
import com.koi151.listing_services.customExceptions.PropertyNotFoundException;
import com.koi151.listing_services.customExceptions.ServiceUnavailableException;
import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.mapper.PropertyServicePackageMapper;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.repository.PostServicePackageRepository;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.repository.PropertyPostPackageRepository;
import com.koi151.listing_services.repository.PropertyServicePackageRepository;
import com.koi151.listing_services.service.ListingServicesService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ListingServicesServiceImpl implements ListingServicesService {

    private final PropertyPostPackageRepository propertyPostPackageRepository;
    private final PropertyServicePackageRepository propertyServicePackageRepository;
    private final PostServiceRepository postServiceRepository;
    private final PostServicePackageRepository postServicePackageRepository;

    private final PropertyClient propertyClient;

    private final PropertyServicePackageMapper propertyServicePackageMapper;
    private final PostServiceMapper postServiceMapper;
    private final ObjectMapper objectMapper;

    private void postServiceCreateValidate(PropertyServicePackageCreateRequest request) {
        // check whether property already have post package or not
        if (propertyPostPackageRepository.existsByPropertyId(request.propertyId()))
            throw new DuplicatedPropertyPostPackageException("Property with id: " + request.propertyId()
                + " already has an active post service package");

        // property id check
        try {
            var propertyResponse = propertyClient.propertyExistsCheck(request.propertyId());
            if (!propertyResponse.getStatusCode().is2xxSuccessful() || propertyResponse.getBody() != null) {
                var propertyData = objectMapper.convertValue(
                        Objects.requireNonNull(Objects.requireNonNull(propertyResponse.getBody()).getData()), Boolean.class);

                if (!propertyData)
                    throw new PropertyNotFoundException("Property with id: " + request.propertyId() + " is not found or currently inactive");
            } else {
                throw new ServiceUnavailableException("Property service is unavailable right now");
            }
        } catch (FeignException ex) {
            throw new ServiceUnavailableException("Property service is unavailable right now");
        }

        // post service ids check
        List<Long> propertyPostIdsNotExists = postServiceRepository.findMissingPostServiceIds(request.postServiceIds());
        if (!propertyPostIdsNotExists.isEmpty())
            throw new PostServiceNotExistedException("Property service ids not exists: " + propertyPostIdsNotExists);

    }
    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPostServicePackage(PropertyServicePackageCreateRequest request) {
        postServiceCreateValidate(request);

        // Create and save PropertyServicePackage
        PropertyServicePackage propertyServicePackage = propertyServicePackageMapper.toPropertyServicePackageEntity(request);
        var savedPropertyServicePackage = propertyServicePackageRepository.save(propertyServicePackage);

        // Fetch PostServices in one go base on request service id
        List<PostServiceBasicInfoDTO> postServices = postServiceRepository.getNameAndAvailableUnitsById(request.postServiceIds());

        // Map PostServices info to PostServicePackage entities
        List<PostServicePackage> postServicePackagesEntities = postServices.stream()
            .map(postService -> PostServicePackage.builder() // use builder instead of mapstruct for reducing code complexity in this cae
                .postServicePackageKey(PostServicePackageKey.builder()
                    .postServiceId(postService.postServiceId())
                    .propertyServicePackageId(savedPropertyServicePackage.getPropertyServicePackageId())
                    .build())
                .postService(postServiceMapper.toPostServiceEntity(postService))
                .propertyServicePackage(savedPropertyServicePackage)
                .unitsRemaining(postService.availableUnits())
                .build())
            .toList();

        // Save PostServicePackages in batch
        postServicePackageRepository.saveAll(postServicePackagesEntities);

        return propertyServicePackageMapper.toPropertyServicePackageCreateDTO(propertyServicePackage, postServices);
    }
}


