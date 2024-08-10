package com.koi151.listing_services.service.converter;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.mapper.ListingServiceMapper;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListingServiceConverter {

    private final ListingServiceMapper listingServiceMapper;

    public PostService toPostServiceEntity(PostServiceCreateRequest request) {
        PostService entity = listingServiceMapper.toPostServiceEntity(request);
        entity.getPostServicePricing().setPostService(entity); // pricing always != null due to BaseEntity extends
        entity.getPromotions().forEach(promotionEntity ->
                promotionEntity.setPostService(entity));
        return entity;
    }

}
