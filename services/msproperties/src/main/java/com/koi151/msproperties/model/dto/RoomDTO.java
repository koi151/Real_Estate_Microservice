package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.entity.RoomEntity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private long roomId;
    private int propertyId;
    private String roomType;
    private int quantity;
}
