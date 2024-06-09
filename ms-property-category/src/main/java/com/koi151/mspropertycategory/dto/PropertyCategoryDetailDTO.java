package com.koi151.mspropertycategory.dto;

import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCategoryDetailDTO {

    @NotEmpty(message = "Cannot get property category title")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private List<String> imageUrls;

    public PropertyCategoryDetailDTO(PropertyCategory propertyCategory) {
        this.title = propertyCategory.getTitle();
        this.description = propertyCategory.getDescription();
        this.imageUrls = List.of(propertyCategory.getImageUrls().split(","));
        this.status = propertyCategory.getStatus();
    }
}
