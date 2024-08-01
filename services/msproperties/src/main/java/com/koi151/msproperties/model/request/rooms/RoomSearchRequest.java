package com.koi151.msproperties.model.request.rooms;

import com.koi151.msproperties.enums.RoomTypeEnum;
import jakarta.validation.constraints.*;

public record RoomSearchRequest(
    Long roomId,
    @Size(max = 50, message = "{roomType} is invalid room type")
    RoomTypeEnum roomType,
    @PositiveOrZero(message = "Room quantity searching must be non-negative value")
    @Max(value = 999, message = "{roomType} quantity cannot exceed 999")
    Short quantity
) {}
