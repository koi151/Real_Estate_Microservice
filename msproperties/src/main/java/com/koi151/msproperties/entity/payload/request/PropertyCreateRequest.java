package com.koi151.msproperties.entity.payload.request;

import com.koi151.msproperties.entity.Direction;
import com.koi151.msproperties.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PropertyCreateRequest {

    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @NotNull(message = "Category id cannot be null")
    @Positive(message = "Category id must be positive")
    private Integer categoryId;

    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private Float area;

    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Float price;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    private Integer totalFloor;

    @Enumerated(EnumType.STRING)
    private Direction houseDirection;

    @Enumerated(EnumType.STRING)
    private Direction balconyDirection;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @NotEmpty(message = "Available time cannot be empty")
    private String availableFrom;

    private MultipartFile images;
}
