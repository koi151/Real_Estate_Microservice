package com.koi151.msproperties.model.request.propertyPostService;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.enums.DaysPostedEnum;
import com.koi151.msproperties.enums.PostingPackageEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder(builderClassName = "PropertyPostServiceCreateRequestBuilder", toBuilder = true)
@Jacksonized // for working with Jackson annotation - @JsonFormat
public class PropertyPostServiceCreateRequest {

    @NotNull(message = "Expire date of post cannot be null")
    @Future(message = "Expire date must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postingDate;

    private PostingPackageEnum postingPackage;
    private DaysPostedEnum daysPosted;

    @PositiveOrZero(message = "Priority pushes time must be positive or zero")
    @Max(value = 10000, message = "Priority pushes cannot exceed 10000")
    private short priorityPushes;
}


