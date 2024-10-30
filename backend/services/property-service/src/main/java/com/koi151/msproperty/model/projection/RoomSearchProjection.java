package com.koi151.msproperty.model.projection;

import com.koi151.msproperty.enums.RoomTypeEnum;

public record RoomSearchProjection (
//    Long propertyId,
    RoomTypeEnum roomType,
    Short quantity
){}


