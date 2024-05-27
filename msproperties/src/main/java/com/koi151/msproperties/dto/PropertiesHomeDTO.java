package com.koi151.msproperties.dto;

import com.koi151.msproperties.entity.Properties;
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
    private String status;
    private int view;
    private boolean deleted;
}
