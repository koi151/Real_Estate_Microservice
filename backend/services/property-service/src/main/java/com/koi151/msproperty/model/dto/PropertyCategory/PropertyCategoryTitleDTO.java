package com.koi151.msproperty.model.dto.PropertyCategory;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PropertyCategoryTitleDTO {

    @NotEmpty(message = "Cannot get category title")
    private String title;
}

