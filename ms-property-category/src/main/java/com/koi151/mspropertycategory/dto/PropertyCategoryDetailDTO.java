package com.koi151.mspropertycategory.dto;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCategoryDetailDTO {

    @NotEmpty(message = "Cannot get property category title")
    private String title;

    private String description;
    private String images;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    public PropertyCategoryDetailDTO(PropertyCategory propertyCategory) {
        this.title = propertyCategory.getTitle();
        this.description = propertyCategory.getDescription();
        this.images = propertyCategory.getImageUrls();
        this.statusEnum = propertyCategory.getStatus();
    }
}
