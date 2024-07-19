package com.koi151.msproperties.model.request.propertyCategory;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.validation.constraints.Size;

public record PropertyCategorySearchRequest (

    @Size(max = 100, message = "Title must be at most 100 characters long")
    String title,

    @Size(max = 1000, message = "Description search must be at most {max} characters long")
    String description,

    StatusEnum status,
    boolean deleted
) {}
