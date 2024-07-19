package com.koi151.msproperties.model.request.propertyPostService;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.enums.DaysPostedEnum;
import com.koi151.msproperties.enums.PostingPackageEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder(builderClassName = "PropertyPostServiceCreateRequestBuilder", toBuilder = true)
@Jacksonized // for working with Jackson annotation - @JsonFormat
public record PropertyPostServiceCreateRequest (

    @Future(message = "Posting date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime postingDate,

    PostingPackageEnum postingPackage,
    DaysPostedEnum daysPosted,
    @PositiveOrZero(message = "Priority pushes time must be positive or zero")
    @Max(value = 10000, message = "Priority pushes cannot exceed 10000")
    short priorityPushes
) {}


