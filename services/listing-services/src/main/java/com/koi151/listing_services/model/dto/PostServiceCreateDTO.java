package com.koi151.listing_services.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostServiceCreateDTO {

    private Long post_service_id;
    private Long postServiceCategoryId;
    private PostServicePricingDTO postServicePricing;
    private List<PromotionDTO> promotions;
    private String name;
    private String status;
    private String description;
    private LocalDateTime expiredAt;
    private LocalDateTime postingDate;
}
