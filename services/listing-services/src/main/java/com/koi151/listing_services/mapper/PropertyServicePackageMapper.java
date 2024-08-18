package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface PropertyServicePackageMapper {

    PropertyServicePackage toPropertyServicePackageEntity(PropertyServicePackageCreateRequest request);

    @Mapping(target = "packageType", source = "entity.packageType.packageName")
    @Mapping(target = "status", source = "entity.status.statusName")
    @Mapping(target = "postServices.postServicePricings.packageType", source = "postServices")
    PropertyServicePackageCreateDTO toPropertyServicePackageCreateDTO(PropertyServicePackage entity, List<PostServiceBasicInfoDTO> postServices);
}