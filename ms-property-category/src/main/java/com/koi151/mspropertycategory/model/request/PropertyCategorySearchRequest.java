package com.koi151.mspropertycategory.model.request;

import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategorySearchRequest {

    private int categoryId;
    @Max(value = 100, message = "Title max length must not exceed 100 characters")
    private String title;
    private String description;
    private StatusEnum status;
    private boolean deleted;
}
