package com.koi151.mspropertycategory.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Properties {

    private String title;
    private String images;
    private String description;
    private String status;
    private int view;
    private boolean deleted;
}
