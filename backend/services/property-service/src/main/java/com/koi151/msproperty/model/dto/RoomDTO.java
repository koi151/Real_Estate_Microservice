package com.koi151.msproperty.model.dto;

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
