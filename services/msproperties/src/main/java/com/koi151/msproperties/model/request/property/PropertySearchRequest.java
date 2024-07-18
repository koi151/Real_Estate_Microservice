package com.koi151.msproperties.model.request.property;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearchRequest {

    private String title;

    @Enumerated(EnumType.STRING)
    private PropertyTypeEnum type;

    @Enumerated(EnumType.STRING)
    private PaymentScheduleEnum paymentSchedule;

    @Positive(message = "Category id search request must be positive")
    private Integer categoryId;

    @PositiveOrZero(message = "Min area must be positive or zero")
    private Float areaFrom;

    @PositiveOrZero(message = "Max area must be positive or zero")
    private Float areaTo;

    @PositiveOrZero(message = "Min price must be positive or zero")
    private Double priceFrom;

    @PositiveOrZero(message = "Max price must be positive or zero")
    private Double priceTo;

    private String description;

    @PositiveOrZero(message = "Total floor search request must be positive or zero")
    private Integer totalFloor;

    @Enumerated(EnumType.STRING)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    private DirectionEnum balconyDirection;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    private String availableFrom; ////

    private String term;

    private String city;
    private String district;
    private String ward;
    private String street;

    @PositiveOrZero(message = "Number of bedrooms must be positive or zero")
    private Integer bedrooms;

    @PositiveOrZero(message = "Number of bathrooms must be positive or zero")
    private Integer bathrooms;

    @PositiveOrZero(message = "Number of kitchens must be positive or zero")
    private Integer kitchens;
}