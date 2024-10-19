package com.koi151.msproperty.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertiesHomeDTO {

    private String title;
    private String imageUrls;
    private String description;
    private int view;
    private String status;
}
