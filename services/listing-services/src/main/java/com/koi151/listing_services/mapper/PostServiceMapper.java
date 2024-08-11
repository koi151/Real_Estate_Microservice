package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.entity.PostServicePricing;
import com.koi151.listing_services.entity.Promotion;
import com.koi151.listing_services.model.dto.PostServiceCreateDTO;
import com.koi151.listing_services.model.dto.PostServicePricingCreateDTO;
import com.koi151.listing_services.model.dto.PromotionCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import com.koi151.listing_services.model.request.PostServicePricingCreateRequest;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface PostServiceMapper {

    PostService toPostServiceEntity(PostServiceCreateRequest request);

    @Mapping(target = "postServicePricings.packageType", expression = "java(mapPostServicePricingsDTO(entity.getPostServicePricings))")
    @Mapping(target = "promotions.packageType", expression = "java(mapPostServicePromotionsCreateDTO(entity.getPromotions))")
    @Mapping(target = "postServiceId", source = "postServiceId")
    PostServiceCreateDTO toPostServiceCreateDTO(PostService entity);

    @Mapping(target = "packageType", expression = "java(entity.getPackageType().getPackageName())")
    PostServicePricingCreateDTO toPostServicePricingCreateDTO(PostServicePricing entity);

    @Mapping(target = "packageType", expression = "java(entity.getPackageType().getPackageName())")
    PromotionCreateDTO toPromotionsCreateDTO(Promotion entity);

    default List<PostServicePricingCreateDTO> mapPostServicePricingsCreateDTO(List<PostServicePricing> entities) {
        return entities == null ? null
            : entities.stream()
                .map(this::toPostServicePricingCreateDTO)
                .toList();
    }

    default List<PromotionCreateDTO> mapPostServicePromotionsCreateDTO(List<Promotion> entities) {
        return entities == null ? null
            : entities.stream()
            .map(this::toPromotionsCreateDTO)
            .toList();
    }
}
