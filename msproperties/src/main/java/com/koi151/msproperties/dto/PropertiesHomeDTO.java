package com.koi151.msproperties.dto;

import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertiesHomeDTO {

    private String title;
    private String images;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    private int view;
}
