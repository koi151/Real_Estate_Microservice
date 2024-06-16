package com.koi151.msproperties.model.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoomResponse {
    private String roomType;
    private int quantity;
}
