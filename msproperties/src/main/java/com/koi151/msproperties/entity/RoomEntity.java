package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @NotNull(message = "Property id cannot be null")
    private PropertyEntity propertyEntity;

    @Column(name = "room_type", nullable = false, length = 50)
    @NotEmpty(message = "Room type cannot be empty")
    @Size(max = 50, message = "Room type cannot be longer than 50 characters")
    private String roomType;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT")
    @NotNull(message = "Room quantity cannot be empty")
    private int quantity;
}
