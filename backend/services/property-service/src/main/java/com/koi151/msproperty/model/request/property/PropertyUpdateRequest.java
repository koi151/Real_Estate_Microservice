package com.koi151.msproperty.model.request.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.koi151.msproperty.enums.DirectionEnum;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentUpdateRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleUpdateRequest;
import com.koi151.msproperty.model.request.propertyPostService.PropertyPostServiceCreateUpdateRequest;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.model.request.address.AddressUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder(builderClassName = "PropertyCreateRequestBuilder", toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PropertyUpdateRequest (

    @Size(min = 5, max = 100, message = "Title length must between {min} and {max} characters")
    String title,

    Long categoryId,
    String accountId,

    @Valid
    PropertyForSaleUpdateRequest propertyForSale,
    @Valid
    PropertyForRentUpdateRequest propertyForRent,
    @Valid
    List<RoomCreateUpdateRequest> rooms,
    @Valid
    AddressUpdateRequest address,
    @Valid
    PropertyPostServiceCreateUpdateRequest propertyPostService,

    @PositiveOrZero(message = "Area must be positive or zero")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    Float area,

    String description,

    @PositiveOrZero(message = "Total floor must be positive or zero")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    Short totalFloor,

    DirectionEnum houseDirection,
    DirectionEnum balconyDirection,
    StatusEnum status,

    List<String> imageUrlsRemove
) {}
