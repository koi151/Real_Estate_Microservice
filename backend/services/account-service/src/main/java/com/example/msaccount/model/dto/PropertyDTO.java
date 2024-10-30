package com.example.msaccount.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class PropertyDTO {
    // used to get data from Properties application -> No validation needed

    private String title;
    private String imageUrls;
    private String description;
    private String status;
    private int view;
    private boolean deleted;
}
