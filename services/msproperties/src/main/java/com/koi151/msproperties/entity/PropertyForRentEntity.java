package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity(name = "property_for_rent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyForRentEntity {

    @Id
    private Long propertyId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "rental_price", nullable = false)
    @NotNull(message = "Rental price cannot be null")
    @PositiveOrZero(message = "Rental price must be positive or zero")
    private double rentalPrice;

    @Column(name = "payment_schedule", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment schedule cannot be null")
    private PaymentScheduleEnum paymentSchedule;

    @Column(name = "rental_terms", columnDefinition = "TEXT")
    private String rentalTerms;
}
