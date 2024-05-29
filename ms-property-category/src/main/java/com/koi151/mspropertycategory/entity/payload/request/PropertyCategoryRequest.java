package com.koi151.mspropertycategory.entity.payload.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PropertyCategoryRequest {

    private String title;
    private String description;
    private String images;
    private String status;
    private MultipartFile file;
    private String fileName;
    private ImageModel imageModel;
}
