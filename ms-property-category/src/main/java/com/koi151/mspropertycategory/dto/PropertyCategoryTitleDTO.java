package com.koi151.mspropertycategory.dto;

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

