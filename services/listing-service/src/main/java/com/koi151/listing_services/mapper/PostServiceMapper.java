package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PostServicePricing;
import com.koi151.listing_services.entity.Promotion;
import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import com.koi151.listing_services.model.dto.*;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
//        builder = @Builder(disableBuilder = true)
)
public interface PostServiceMapper {

    PostService toPostServiceEntity(PostServiceCreateRequest request);

    PostService toPostServiceEntity(PostServiceBasicInfoDTO request);

    @Mapping(target = "postServicePricings.packageType", expression = "java(mapPostServicePricingsDTO(entity.getPostServicePricings))")
    @Mapping(target = "promotions.packageType", expression = "java(mapPostServicePromotionsCreateDTO(entity.getPromotions))")
    @Mapping(target = "postServiceId", source = "postServiceId")
    PostServiceCreateDTO toPostServiceCreateDTO(PostService entity);

    @Mapping(target = "packageType", expression = "java(entity.getPackageType().getPackageName())")
    PostServicePricingCreateDTO toPostServicePricingCreateDTO(PostServicePricing entity);

    @Mapping(target = "packageType", expression = "java(entity.getPackageType().getPackageName())")
    PromotionCreateDTO toPromotionsCreateDTO(Promotion entity);


    PostServicePackage toPostServicePackageEntity(PostServiceBasicInfoDTO postService, PostServicePackageKey postServicePackageKey);

    @Mapping(target = "propertyServicePackageId", source = "propertyServicePackageId")
    @Mapping(target = "postServiceId", source = "postService.postServiceId")
    PostServicePackageKey toPostServicePackageKey(PostService postService, Long propertyServicePackageId);
    //--------
    default PostServicePackageKey createPostServicePackageKey(Long propertyPackageId, Long postServiceId) {
        return PostServicePackageKey.builder()
                .propertyServicePackageId(propertyPackageId)
                .postServiceId(postServiceId)
                .build();
    }

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
