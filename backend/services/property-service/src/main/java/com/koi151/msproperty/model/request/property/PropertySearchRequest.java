package com.koi151.msproperty.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperty.enums.DirectionEnum;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.request.address.AddressSearchRequest;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentSearchRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleSearchRequest;
import com.koi151.msproperty.model.request.rooms.RoomSearchRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record PropertySearchRequest (
    @Size(max = 100, message = "Title searching must at most {max} characters")
    String title,
    @Valid
    PropertyForRentSearchRequest propertyForRent,
    @Valid
    PropertyForSaleSearchRequest propertyForSale,
    @Valid
    PropertyCategorySearchRequest propertyCategory,
    DirectionEnum houseDirection,
    DirectionEnum balconyDirection,
    StatusEnum status,
    @PositiveOrZero(message = "Min area must be non-negative value")
    BigDecimal areaFrom,

    @PositiveOrZero(message = "Area searching value must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area searching value cannot exceed 99,999,999.99")
    BigDecimal areaTo,

    @Size(max = 1000, message = "Description search must be at most {max} characters long")
    String description,

    @PositiveOrZero(message = "Total floor searching request must be non-negative value")
    @Max(value = 999, message = "Total floor search value cannot exceed 999")
    Short totalFloor,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String availableFrom,

    @Size(max = 1000, message = "Term searching cannot exceed 1000 characters long")
    String term,

    @Valid
    AddressSearchRequest address,
    @Valid
    List<RoomSearchRequest> rooms
){}