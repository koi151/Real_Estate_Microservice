package com.koi151.msproperties.model.request.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.address.AddressSearchRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PropertySearchRequest (

    @Size(max = 100, message = "Title search must at most {max} characters")
    String title,
    PropertyTypeEnum type,
    PaymentScheduleEnum paymentSchedule,
    DirectionEnum houseDirection,
    DirectionEnum balconyDirection,
    StatusEnum status,

    Integer categoryId,
    @PositiveOrZero(message = "Min area must be non-negative value")
    BigDecimal areaFrom,

    @PositiveOrZero(message = "Max area must be non-negative value")
    @DecimalMax(value = "99_999_999.99", message = "Area search value cannot exceed 99,999,999.99")
    BigDecimal areaTo,

    @PositiveOrZero(message = "Min price must be non-negative value")
    Long priceFrom,

    @PositiveOrZero(message = "Max price must be non-negative value")
    @DecimalMax(value = "99_999_999_999", message = "Max price search cannot exceed 99,999,999,999")
    Long priceTo,

    @Size(max = 1000, message = "Description search must be at most {max} characters long")
    String description,

    @PositiveOrZero(message = "Total floor search request must be non-negative value")
    @Max(value = 999, message = "Total floor search value cannot exceed 999")
    Short totalFloor,

    @Size(max = 5, message = "Property available time is invalid")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd")
    String availableFrom, //

    @Size(max = 1000, message = "Term search cannot exceed 1000 characters long")
    String term,

    @Valid // need update
    AddressSearchRequest addressSearchRequest,

    @PositiveOrZero(message = "Number of bedrooms must be non-negative value")
    @Max(value = 999, message = "Number of bedroom search cannot exceed {max}")
    Short bedrooms,

    @PositiveOrZero(message = "Number of bathrooms must be non-negative value")
    @Max(value = 999, message = "Number of bathrooms search cannot exceed {max}")
    Short bathrooms,

    @PositiveOrZero(message = "Number of kitchens must be non-negative value")
    @Max(value = 999, message = "Number of kitchens search cannot exceed {max}")
    Short kitchens
){}