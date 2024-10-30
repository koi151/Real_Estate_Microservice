package com.koi151.msproperty.model.request.rooms;

import com.koi151.msproperty.enums.RoomTypeEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RoomCreateUpdateRequest(
    Long roomId,
    @NotNull(message = "Room type is mandatory")
    RoomTypeEnum roomType,

    @NotNull(message = "Room quantity is mandatory")
    @PositiveOrZero(message = "Room quantity must be non-negative value")
    @Max(value = 999, message = "{roomType} quantity cannot exceed 999")
    Short quantity
) {}
