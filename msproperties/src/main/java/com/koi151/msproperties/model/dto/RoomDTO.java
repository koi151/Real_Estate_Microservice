package com.koi151.msproperties.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private int roomId;
    private int propertyId;
    private String roomType;
    private int quantity;


}
