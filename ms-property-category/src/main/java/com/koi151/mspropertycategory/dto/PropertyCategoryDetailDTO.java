package com.koi151.mspropertycategory.dto;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCategoryDetailDTO {

    private String title;
    private String description;
    private String images;
    private String status;

    public PropertyCategoryDetailDTO(PropertyCategory propertyCategory) {
        this.title = propertyCategory.getTitle();
        this.description = propertyCategory.getDescription();
        this.images = propertyCategory.getImages();
        this.status = propertyCategory.getStatus();
    }
}
