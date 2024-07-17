package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.PostingPackageEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "property_post_service")
public class PropertyPostServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyPostServiceId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "expireDate", columnDefinition = "TIMESTAMP(0)", nullable = false)
    @NotNull(message = "Expire date of post cannot be null")
    @Future(message = "Expire date must be in the future")
    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "posting_package", nullable = false)
    private PostingPackageEnum postingPackage;

    @Column(name = "priority_pushes")
    @PositiveOrZero(message = "Priority pushes time must be positive or zero")
    @Max(value = 32000, message = "Priority pushes cannot exceed 32000")
    private short priorityPushes;
}
