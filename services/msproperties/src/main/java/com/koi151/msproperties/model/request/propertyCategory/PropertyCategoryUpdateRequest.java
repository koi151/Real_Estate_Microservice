package com.koi151.msproperties.model.request.propertyCategory;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PropertyCategoryUpdateRequest {
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    Set<String> imageUrlsRemove;
}
