package com.koi151.mspropertycategory.entity.payload.request;

import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PropertyCategoryRequest {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    private MultipartFile images;
}
