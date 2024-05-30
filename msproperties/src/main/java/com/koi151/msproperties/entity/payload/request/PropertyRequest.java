package com.koi151.msproperties.entity.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PropertyRequest {

    @NotEmpty(message = "Title is empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @NotNull(message = "Category id is empty")
    private Integer categoryId;

    @NotNull(message = "Area is empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private Float area;

    @NotNull(message = "Price is empty")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Float price;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    private Integer totalFloor;

    private String houseDirection;
    private String balconyDirection;

    private String availableFrom;
    private String status;
    private MultipartFile images;
}
