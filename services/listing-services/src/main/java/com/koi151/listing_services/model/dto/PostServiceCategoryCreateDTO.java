package com.koi151.listing_services.model.dto;

import com.koi151.listing_services.enums.Status;
import lombok.Data;

@Data
public class PostServiceCategoryCreateDTO {
    private Long postServiceCategoryId;
    private String name;
    private Status status;
    private String description;
}
