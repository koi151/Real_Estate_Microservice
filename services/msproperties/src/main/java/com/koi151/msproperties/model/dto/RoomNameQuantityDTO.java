package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.enums.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RoomNameQuantityDTO {
    private String roomType;
    private Short quantity;
}
