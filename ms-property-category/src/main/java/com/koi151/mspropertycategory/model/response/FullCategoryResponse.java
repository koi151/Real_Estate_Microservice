package com.koi151.mspropertycategory.model.response;

import com.koi151.mspropertycategory.entity.Properties;
import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullCategoryResponse {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    List<Properties> properties;
}
