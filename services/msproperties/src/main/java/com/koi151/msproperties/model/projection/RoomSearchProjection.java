package com.koi151.msproperties.model.projection;

import com.koi151.msproperties.enums.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RoomSearchProjection {
    private RoomTypeEnum roomType;
    private Short quantity;

}
