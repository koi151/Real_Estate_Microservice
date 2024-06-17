package com.koi151.mspropertycategory.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyEntity {
    // used to get data from Properties application -> No validation needed

    private String title;
    private String imageUrls;
    private String description;
    private StatusEnum status = StatusEnum.ACTIVE;
    private int view;

    private boolean deleted;
}
