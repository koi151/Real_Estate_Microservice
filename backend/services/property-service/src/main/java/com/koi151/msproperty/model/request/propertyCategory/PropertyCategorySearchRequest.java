package com.koi151.msproperty.model.request.propertyCategory;

import com.koi151.msproperty.enums.StatusEnum;
import jakarta.validation.constraints.Size;

public record PropertyCategorySearchRequest (

    @Size(max = 100, message = "Category title searching must be at most {max} characters long")
    String title,
    Long categoryId,
    @Size(max = 1000, message = "Category searching must be at most {max} characters long")
    String description,
    StatusEnum status,
    Boolean deleted
) {}
