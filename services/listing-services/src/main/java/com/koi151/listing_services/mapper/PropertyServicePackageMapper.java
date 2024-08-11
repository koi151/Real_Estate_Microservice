package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface PropertyServicePackageMapper {
    PropertyServicePackage toPostServicePackageEntity(PropertyServicePackageCreateRequest request);
    PropertyServicePackageCreateDTO toPropertyServicePackageCreateDTO(PropertyServicePackage entity);
}
