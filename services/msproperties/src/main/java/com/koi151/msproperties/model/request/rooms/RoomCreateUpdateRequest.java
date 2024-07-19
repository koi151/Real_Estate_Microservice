package com.koi151.msproperties.model.request.rooms;

import com.koi151.msproperties.entity.PropertyEntity;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public record RoomCreateUpdateRequest(

    Long roomId,

    @NotEmpty(message = "Room type is mandatory")
    @Size(max = 50, message = "Room type cannot exceed 50 characters")
    String roomType,

    @NotNull(message = "Room quantity is mandatory")
    @PositiveOrZero(message = "Room quantity must be non-negative value")
    @Max(value = 999, message = "Room quantity cannot exceed 999")
    Short quantity
) {}
