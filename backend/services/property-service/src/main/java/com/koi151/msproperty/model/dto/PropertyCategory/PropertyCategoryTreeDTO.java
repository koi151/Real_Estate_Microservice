package com.koi151.msproperty.model.dto.PropertyCategory;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PropertyCategoryTreeDTO {
    private Long id;
    private String title;
    private List<PropertyCategoryTreeDTO> children;
}
