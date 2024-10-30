package com.koi151.msproperty.entity;

import com.koi151.msproperty.enums.RoomTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    indexes = {
        @Index(name = "idx_room_property_id", columnList = "property_id"),
        @Index(name = "idx_room_room_type", columnList = "room_type"),
        @Index(name = "idx_room_quantity", columnList = "quantity")
    }
)
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @NotNull(message = "Property id is mandatory")
    private Property property;

    @Column(name = "room_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    @NotNull(message = "Room quantity is mandatory")
    @Max(value = 999, message = "{roomType} quantity cannot exceed 999")
    private short quantity;
}
