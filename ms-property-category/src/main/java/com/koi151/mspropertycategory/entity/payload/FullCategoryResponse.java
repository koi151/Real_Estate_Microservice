package com.koi151.mspropertycategory.entity.payload;

import com.koi151.mspropertycategory.entity.Properties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FullCategoryResponse {

    private String title;
    private String description;
    private String status;

    List<Properties> properties;
}
