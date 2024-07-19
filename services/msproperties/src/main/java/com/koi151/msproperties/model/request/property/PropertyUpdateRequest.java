package com.koi151.msproperties.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.annotations.LocalDatePattern;
import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentUpdateRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleUpdateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.model.request.address.AddressUpdateRequest;
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
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private Float area;

    private String description;

    @PositiveOrZero(message = "Total floor must be positive or zero")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    private Integer totalFloor;

    private DirectionEnum houseDirection;
    private DirectionEnum balconyDirection;
    private StatusEnum status;

    @LocalDatePattern(message = "Property available date must be in 'yyyy-MM-dd' format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String availableFrom;

    private List<String> imageUrlsRemove;
}
