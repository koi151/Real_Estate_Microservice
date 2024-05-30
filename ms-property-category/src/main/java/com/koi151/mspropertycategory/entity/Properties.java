package com.koi151.mspropertycategory.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Properties {
    // used to get data from Properties application -> No validation needed

    private String title;
    private String imageUrls;
    private String description;
    private Status status = Status.ACTIVE;
    private int view;

    private boolean deleted;
}
