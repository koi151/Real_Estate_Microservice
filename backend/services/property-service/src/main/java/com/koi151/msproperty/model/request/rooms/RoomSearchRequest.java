package com.koi151.msproperty.model.request.rooms;

import com.koi151.msproperty.enums.RoomTypeEnum;
import jakarta.validation.constraints.*;

public record RoomSearchRequest(
    Long roomId,
    RoomTypeEnum roomType,
    @PositiveOrZero(message = "Room quantity searching must be non-negative value")
    @Max(value = 999, message = "{roomType} quantity cannot exceed 999")
    Short quantity
) {}
