package com.koi151.msproperty.model.dto.PropertyCategory;

import com.koi151.msproperty.enums.CategoryStatusEnum;
import com.koi151.msproperty.enums.StatusEnum;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCategoryDetailDTO {

    private String title;
    private String description;
    private CategoryStatusEnum status;
    private List<String> imageUrls;
}
