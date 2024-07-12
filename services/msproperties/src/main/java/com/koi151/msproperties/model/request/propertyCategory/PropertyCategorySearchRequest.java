package com.koi151.msproperties.model.request.propertyCategory;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCategorySearchRequest {

    private int categoryId;

    @Size(max = 100, message = "Title must be at most 100 characters long")
    private String title;

    private String description;
    private StatusEnum status;
    private boolean deleted;
}
