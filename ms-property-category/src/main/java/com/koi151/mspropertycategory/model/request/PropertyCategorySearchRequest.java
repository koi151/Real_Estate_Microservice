package com.koi151.mspropertycategory.model.request;

import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategorySearchRequest {

    private int categoryId;

    @Size(max = 100, message = "Title must be at most 100 characters long")
    private String title;

    private String description;
    private StatusEnum status;
    private boolean deleted;
}
