package com.koi151.msproperties.entity.payload.request;

import com.koi151.msproperties.entity.DirectionEnum;
import com.koi151.msproperties.entity.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class PropertyUpdateRequest {

    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Positive(message = "Category id must be positive")
    private Integer categoryId;

    @PositiveOrZero(message = "Area must be positive or zero")
    private Float area;

    @PositiveOrZero(message = "Price must be positive or zero")
    private Float price;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    private Integer totalFloor;

    @Enumerated(EnumType.STRING)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    private DirectionEnum balconyDirection;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private String availableFrom;

    Set<String> imageUrlsRemove;
}
