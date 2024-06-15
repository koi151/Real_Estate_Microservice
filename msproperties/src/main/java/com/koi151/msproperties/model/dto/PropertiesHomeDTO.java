package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.enums.StatusEnum;
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
