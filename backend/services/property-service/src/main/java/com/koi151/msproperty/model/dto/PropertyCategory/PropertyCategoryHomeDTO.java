package com.koi151.msproperty.model.dto.PropertyCategory;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class PropertyCategoryHomeDTO {
    private Long categoryId;
    private String title;
    private String description;
    private String imageUrls;
    private String status;
    private LocalDateTime createdDate;
}
