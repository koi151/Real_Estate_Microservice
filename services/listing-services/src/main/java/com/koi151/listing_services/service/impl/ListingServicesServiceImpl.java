package com.koi151.listing_services.service.impl;

import com.koi151.listing_services.customExceptions.PostServiceNotExistedException;
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

    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPostServicePackage(PropertyServicePackageCreateRequest request) {
//        if (!postServiceRepository.existsAllByPostServiceIdIn(request.postServicePackageIds())) {
            // find ids that not exists

        List<Long> propertyPostIdsNotExists = postServiceRepository.findIdsByPostServiceIdNotIn(request.postServicePackageIds());
        if (propertyPostIdsNotExists != null)
            throw new PostServiceNotExistedException("Property post ids not exists: " + propertyPostIdsNotExists);


        List<PostService> existingServices = postServiceRepository.findAllById(request.postServicePackageIds());
        Set<Long> existingIds = existingServices.stream()
                .map(PostService::getPostServiceId)
                .collect(Collectors.toSet());

        List<Long> missingIds = request.postServicePackageIds().stream()
                .filter(id -> !existingIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new PostServiceNotExistedException("The following PostService IDs do not exist: " + missingIds);
        }

        PropertyServicePackage postServicePackage = propertyServicePackageMapper.toPostServicePackageEntity(request);
        propertyPostPackageRepository.save(postServicePackage);
        return propertyServicePackageMapper.toPropertyServicePackageCreateDTO(postServicePackage);
    }
}






