package com.koi151.msproperties.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoomNameQuantityDTO {
    private String roomType;
    private int quantity;
}
