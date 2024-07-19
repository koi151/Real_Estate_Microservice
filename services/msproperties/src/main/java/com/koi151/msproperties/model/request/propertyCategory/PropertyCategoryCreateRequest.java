package com.koi151.msproperties.model.request.propertyCategory;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategoryCreateRequest {

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Size(max = 2000, message = "Property category description cannot exceed {max} characters")
    private String description;
    private StatusEnum status;
}
