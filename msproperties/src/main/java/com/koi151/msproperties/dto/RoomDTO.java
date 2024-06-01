package com.koi151.msproperties.dto;

import lombok.Builder;

@Builder
public class RoomDTO {
    private int roomId;
    private int propertyId;
    private String roomType;
    private int quantity;
}
