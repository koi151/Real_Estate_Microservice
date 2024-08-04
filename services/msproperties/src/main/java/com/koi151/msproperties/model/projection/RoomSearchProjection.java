package com.koi151.msproperties.model.projection;

import com.koi151.msproperties.enums.RoomTypeEnum;

public record RoomSearchProjection (
//    Long propertyId,
    RoomTypeEnum roomType,
    Short quantity
){}


