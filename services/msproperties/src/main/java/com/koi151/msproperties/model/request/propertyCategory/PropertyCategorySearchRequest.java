package com.koi151.msproperties.model.request.propertyCategory;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyCategorySearchRequest {

//    private int categoryId;

    @Size(max = 100, message = "Title must be at most 100 characters long")
    private String title;

    @Size(max = 3000, message = "Description must be at most {max} characters long")
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private boolean deleted;
}
