package com.koi151.msproperties.model.request.propertyCategory;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;


public record PropertyCategoryUpdateRequest (

    @NotBlank(message = "Property category title is mandatory")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    String title,
    @Size(max = 2000, message = "Property category description cannot exceed 2000 characters")
    String description,
    StatusEnum status,
    Set<String> imageUrlsRemove
) {}
