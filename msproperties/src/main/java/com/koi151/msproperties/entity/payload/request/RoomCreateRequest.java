package com.koi151.msproperties.entity.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateRequest {

    @NotEmpty(message = "Room type cannot be empty")
    private String roomType;

    @NotNull(message = "Room quantity cannot be null")
    private Integer quantity;
}
