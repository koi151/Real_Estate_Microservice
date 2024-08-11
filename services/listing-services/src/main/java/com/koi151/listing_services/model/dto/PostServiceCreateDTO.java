package com.koi151.listing_services.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostServiceCreateDTO {
    private Long postServiceId;
    private Long postServiceCategoryId;
    private List<PostServicePricingCreateDTO> postServicePricings;
    private List<PromotionCreateDTO> promotions;
    private String name;
    private int availableUnits;
    private String status;
    private String description;
    private LocalDateTime expiredAt;
    private LocalDateTime postingDate;
}
