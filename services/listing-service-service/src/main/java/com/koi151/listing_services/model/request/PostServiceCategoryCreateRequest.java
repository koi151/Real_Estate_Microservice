package com.koi151.listing_services.model.request;

import com.koi151.listing_services.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostServiceCategoryCreateRequest(
    @NotBlank(message = "Post service category name is mandatory")
    @Size(min = 6, max = 100, message = "Post service category name must between {min} and {max} characters")
    String name,
    Status status,
    @Size(max = 2000, message = "Post service category description cannot exceed {max} characters")
    String description
) {}
