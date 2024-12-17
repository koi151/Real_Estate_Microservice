package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PostServicePricing;
import com.koi151.listing_services.entity.Promotion;
import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import com.koi151.listing_services.enums.Status;
import com.koi151.listing_services.model.dto.*;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
//        builder = @Builder(disableBuilder = true)
)
public interface PostServiceMapper {

    @Mapping(target = "status", defaultValue = "ACTIVE")
    PostService toPostServiceEntity(PostServiceCreateRequest request);

    PostService toPostServiceEntity(PostServiceBasicInfoDTO request);

    @Mapping(target = "postServicePricings.packageType", expression = "java(mapPostServicePricingsDTO(entity.getPostServicePricings))")
    @Mapping(target = "postServiceCategoryId", source = "postServiceCategory.postServiceCategoryId")
    @Mapping(target = "promotions.packageType", expression = "java(mapPostServicePromotionsCreateDTO(entity.getPromotions))")
    @Mapping(target = "postServiceId", source = "postServiceId")
    @Mapping(target = "status", source = "status", qualifiedByName = "enumToString")
    PostServiceDTO toPostServiceDTO(PostService entity);

    List<PostServiceDTO> toPostServiceDTOList(List<PostService> entities);

    @Named("mapPostServicePricings")
    default List<PostServicePricingCreateDTO> mapPostServicePricings(List<PostServicePricing> pricings) {
        if (pricings == null) return null;
        return pricings.stream()
            .map(p -> PostServicePricingCreateDTO.builder()
                .price(p.getPrice())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .packageType(p.getPackageType() != null ? p.getPackageType().name() : null)
                .build())
            .collect(Collectors.toList());
    }

    @Named("mapPromotions")
    default List<PromotionCreateDTO> mapPromotions(List<Promotion> promotions) {
        if (promotions == null) return null;
        return promotions.stream()
            .map(p -> PromotionCreateDTO.builder()
                .discountPercentage(p.getDiscountPercentage())
                .priceDiscount(p.getPriceDiscount())
                .packageType(
                    p.getPackageType() != null
                        ? p.getPackageType().name()
                        : null
                )
                .build())
            .collect(Collectors.toList());
    }

    @Named("enumToString")
    default String enumToString(Status status) {
        return status != null ? status.name() : null;
    }

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
