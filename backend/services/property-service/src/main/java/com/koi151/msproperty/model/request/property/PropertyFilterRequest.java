package com.koi151.msproperty.model.request.property;

import com.koi151.msproperty.enums.DirectionEnum;
import com.koi151.msproperty.enums.PropertyTypeEnum;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.request.address.AddressFilterRequest;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentFilterRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleFilterRequest;
import com.koi151.msproperty.model.request.rooms.RoomFilterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PropertyFilterRequest (
    @Size(max = 100, message = "Title searching must at most {max} characters")
    String title,
    Long categoryId,
    PropertyTypeEnum type,
    @Valid
    PropertyForRentFilterRequest propertyForRent,
    @Valid
    PropertyForSaleFilterRequest propertyForSale,
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

    @PositiveOrZero(message = "Total view searching request must be non-negative value")
    @Max(value = 9999999, message = "View searching value cannot exceed 999")
    Integer view,

    @Size(max = 1000, message = "Term searching cannot exceed {max} characters long")
    String term,

    @PositiveOrZero(message = "Min price must be non-negative")
    BigDecimal priceFrom,

    @PositiveOrZero(message = "Max price must be non-negative")
    BigDecimal priceTo,

    @Valid
    AddressFilterRequest address,

    @Valid
    List<RoomFilterRequest> rooms,

    LocalDateTime createdDate
){}