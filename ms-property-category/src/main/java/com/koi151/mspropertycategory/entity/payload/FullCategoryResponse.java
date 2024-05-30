package com.koi151.mspropertycategory.entity.payload;

import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private Status status;

    List<Properties> properties;
}
