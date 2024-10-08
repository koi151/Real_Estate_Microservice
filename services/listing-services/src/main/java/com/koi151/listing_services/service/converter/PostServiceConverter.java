package com.koi151.listing_services.service.converter;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.model.request.PostServiceCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostServiceConverter {
    private final PostServiceMapper postServiceMapper;

    public PostService toPostServiceEntity(PostServiceCreateRequest request) {
        PostService entity = postServiceMapper.toPostServiceEntity(request);
        if (entity.getPostServicePricings() != null) {
            entity.getPostServicePricings().forEach(postServicePricing ->
                postServicePricing.setPostService(entity));
        }
        entity.getPromotions().forEach(promotionEntity ->
                promotionEntity.setPostService(entity));
        return entity;
    }

}
