package com.koi151.msproperties.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PropertyUpdateRequest { // update to extends from create req

    @Size(min = 5, max = 100, message = "Title length must between {min} and {max} characters")
    private String title;

    private Long categoryId;
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
    @DecimalMax(value = "99_999_999.99", message = "Area cannot exceed 99,999,999.99")
    private Float area;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    private Integer totalFloor;

    private DirectionEnum houseDirection;
    private DirectionEnum balconyDirection;
    private StatusEnum status;

    @Size(max = 5, message = "Property available date must be in 'MM-dd' format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd")
    private String availableFrom;

    private List<String> imageUrlsRemove;
}
