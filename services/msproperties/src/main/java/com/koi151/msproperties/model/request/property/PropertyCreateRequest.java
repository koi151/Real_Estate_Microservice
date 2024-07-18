package com.koi151.msproperties.model.request.property;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperties.model.request.propertyPostService.PropertyPostServiceCreateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.model.request.address.AddressCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class PropertyCreateRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @NotNull(message = "Category id cannot be null")
    @Positive(message = "Category id must be positive")
    private Long categoryId;

    @NotNull(message = "Account id cannot be null")
    @Positive(message = "Account id must be positive")
    private Long accountId;

    @Valid
    private PropertyForSaleCreateRequest propertyForSale;

    @Valid
    private PropertyForRentCreateRequest propertyForRent;

    @Valid
    private List<RoomCreateUpdateRequest> rooms;

    @Valid
    private AddressCreateRequest address;

    @Valid
    private PropertyPostServiceCreateRequest propertyPostService;

    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private Float area;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    private Integer totalFloor;

    private String description;
    private DirectionEnum houseDirection;
    private DirectionEnum balconyDirection;
    private StatusEnum status = StatusEnum.ACTIVE;

    @NotEmpty(message = "Available time cannot be empty")
    private String availableFrom;
}

