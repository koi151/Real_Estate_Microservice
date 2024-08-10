package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.model.dto.PostServiceCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface ListingServiceMapper {

    PostService toPostServiceEntity(PostServiceCreateRequest request);
    PostServiceCreateDTO toPostServiceCreateDTO(PostService entity);
}
