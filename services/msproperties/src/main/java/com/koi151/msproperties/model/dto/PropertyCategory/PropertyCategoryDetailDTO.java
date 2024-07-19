package com.koi151.msproperties.model.dto.PropertyCategory;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    private StatusEnum status;
    private List<String> imageUrls;
}
