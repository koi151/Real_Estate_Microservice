package com.koi151.msproperty.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.koi151.msproperty.enums.*;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperty.model.request.propertyPostService.PropertyPostServiceCreateUpdateRequest;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.model.request.address.AddressCreateRequest;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder(builderClassName = "PropertyCreateRequestBuilder", toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
//@Jacksonized // for working with Jackson annotation - @JsonFormat
public record PropertyCreateRequest(
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    String title,

    @NotNull(message = "Category id is mandatory")
    Long categoryId,

    @NotBlank(message = "String of account id is mandatory")
    String accountId,

    @Valid
    PropertyForSaleCreateRequest propertyForSale,
    @Valid
    PropertyForRentCreateRequest propertyForRent,
    @Valid
    List<RoomCreateUpdateRequest> rooms,
    @Valid
    AddressCreateRequest address,
    @Valid
    PropertyPostServiceCreateUpdateRequest propertyPostService,

    @NotNull(message = "Area value is mandatory")
    @PositiveOrZero(message = "Area must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    BigDecimal area,


    @PositiveOrZero(message = "Total floor must be non-negative value")
    @Max(value = 999, message = "Total floors cannot exceed 999")
    Short totalFloor,

    @Size(max = 5000, message = "Property description cannot exceed 5000 characters")
    String description,

    DirectionEnum houseDirection,
    DirectionEnum balconyDirection,
    StatusEnum status,
    FurnitureEnum furniture,
    LegalDocumentEnum legalDocument,

    // for other service
    PackageType packageType,
    @NotNull(message = "Post service ids used for that property service package is mandatory")
    List<Long> postServiceIds
) {}

