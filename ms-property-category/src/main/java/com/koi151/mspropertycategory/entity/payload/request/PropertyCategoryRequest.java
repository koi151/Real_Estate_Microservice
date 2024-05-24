package com.koi151.mspropertycategory.entity.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyCategoryRequest {

    private String title;
    private String description;
    private String images;
    private String status;
}
