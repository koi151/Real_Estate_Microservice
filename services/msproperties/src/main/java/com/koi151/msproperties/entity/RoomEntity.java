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
    private long roomId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @NotNull(message = "Property id cannot be null")
    private PropertyEntity propertyEntity;

    @Column(name = "room_type", nullable = false, length = 50)
    @NotEmpty(message = "Room type is mandatory")
    @Size(max = 50, message = "Room type cannot exceed 50 characters")
    private String roomType;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    @NotNull(message = "Room quantity is mandatory")
    private int quantity;
}
