package com.koi151.mspropertycategory.model.response;

import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategorySearchResponse {

    private int categoryId;
    private String title;
    private String description;
    private String status;
    private boolean deleted;
}
