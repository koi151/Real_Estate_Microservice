package com.koi151.msproperty.model.request.propertyPostService;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperty.enums.DaysPostedEnum;
import com.koi151.msproperty.enums.PostingPackageEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder(builderClassName = "PropertyPostServiceCreateRequestBuilder", toBuilder = true)
@Jacksonized // for working with Jackson annotation - @JsonFormat
public record PropertyPostServiceCreateUpdateRequest(

    @Future(message = "Posting date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime postingDate,

    PostingPackageEnum postingPackage,
    DaysPostedEnum daysPosted,
    @PositiveOrZero(message = "Priority pushes time must be positive or zero")
    @Max(value = 10000, message = "Priority pushes cannot exceed 10000")
    short priorityPushes
) {}


