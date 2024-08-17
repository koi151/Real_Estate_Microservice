package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.mapper.PropertyServicePackageMapper;
import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.repository.PostServicePackageRepository;
import com.koi151.listing_services.repository.PostServiceRepository;
import com.koi151.listing_services.repository.PropertyServicePackageRepository;
import com.koi151.listing_services.service.ListingServicesService;
import com.koi151.listing_services.validator.PostServiceValidate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ListingServicesServiceImpl implements ListingServicesService {

    private final PropertyServicePackageRepository propertyServicePackageRepository;
    private final PostServiceRepository postServiceRepository;
    private final PostServicePackageRepository postServicePackageRepository;

    private final PropertyServicePackageMapper propertyServicePackageMapper;
    private final PostServiceValidate postServiceValidate;
    private final PostServiceMapper postServiceMapper;

    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPostServicePackage(PropertyServicePackageCreateRequest request) {
        postServiceValidate.postServiceCreateValidate(request);

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

    @Override
    @Transactional
    public PropertyServicePackageSummaryDTO findPropertyServicePackageWithsPostServices(Long id) {
        return propertyServicePackageRepository.findPropertyServicePackageWithsPostServices(id);
    }
}


