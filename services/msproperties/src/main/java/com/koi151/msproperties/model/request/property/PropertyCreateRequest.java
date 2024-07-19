package com.koi151.msproperties.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.math.BigDecimal;
import java.util.List;


public record PropertyCreateRequest(

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    String title,

    @NotNull(message = "Category id is mandatory")
    Long categoryId,

    @NotNull(message = "Account id is mandatory")
    Long accountId,

    @Valid
    PropertyForSaleCreateRequest propertyForSale,
    @Valid
    PropertyForRentCreateRequest propertyForRent,
    @Valid
    List<RoomCreateUpdateRequest> rooms,
    @Valid
    AddressCreateRequest address,
    @Valid
    PropertyPostServiceCreateRequest propertyPostService,

    @NotNull(message = "Area value is mandatory")
    @PositiveOrZero(message = "Area must be non-negative value")
    @DecimalMax(value = "99_999_999.99", message = "Area cannot exceed 99,999,999.99")
    BigDecimal area,

    @PositiveOrZero(message = "Total floor must be non-negative value")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    Short totalFloor,

    @Size(max = 5000, message = "Property description cannot exceed 5000 characters")
    String description,
    DirectionEnum houseDirection,
    DirectionEnum balconyDirection,
    StatusEnum status,
    @NotBlank(message = "Property available date is mandatory")
    @Size(max = 5, message = "Property available date must be in 'MM-dd' format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd")
    String availableFrom
) {}

