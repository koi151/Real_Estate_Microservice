package com.koi151.mspropertycategory.entity.payload.request;

import com.koi151.mspropertycategory.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PropertyCategoryRequest {

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
