package com.koi151.msproperties.model.request.property;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentUpdateRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleUpdateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.model.request.address.AddressUpdateRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class PropertyUpdateRequest {
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Positive(message = "Category id must be positive")
    private Long categoryId;

    @Positive(message = "Account id must be positive")
    private Long accountId;

    @Valid
    private PropertyForSaleUpdateRequest propertyForSale;

    @Valid
    private PropertyForRentUpdateRequest propertyForRent;

    @Valid
    private List<RoomCreateUpdateRequest> rooms;

    @Valid
    private AddressUpdateRequest address;

    @PositiveOrZero(message = "Area must be positive or zero")
    private Float area;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    private Integer totalFloor;

    @Enumerated(EnumType.STRING)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    private DirectionEnum balconyDirection;

    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.ACTIVE;

    private String availableFrom;

    private List<String> imageUrlsRemove;
}
