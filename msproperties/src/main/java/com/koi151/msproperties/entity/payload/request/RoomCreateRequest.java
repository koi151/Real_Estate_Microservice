package com.koi151.msproperties.entity.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCreateRequest {

    @NotEmpty(message = "Room type cannot be empty")
    @Size(max = 50, message = "Room type cannot be longer than 50 characters")
    private String roomType;

    @NotNull(message = "Room quantity cannot be null")
    @PositiveOrZero(message = "Room quantity must be positive or zero")
    private Integer quantity;
}
