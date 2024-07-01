package com.koi151.msproperties.model.request;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCreateRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Enumerated(EnumType.STRING) // enum cannot use @NotEmpty
    @NotNull(message = "Property type cannot be null")
    private PropertyTypeEnum type;

    @Enumerated(EnumType.STRING)
    private PaymentScheduleEnum paymentSchedule;

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
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    private DirectionEnum balconyDirection;

    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.ACTIVE;

    @NotEmpty(message = "Available time cannot be empty")
    private String availableFrom;

    private String term;

    @Valid
    private List<RoomCreateRequest> rooms;

    @Valid
    private AddressRequest address;
}

