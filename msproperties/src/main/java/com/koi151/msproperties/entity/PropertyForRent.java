package com.koi151.msproperties.entity;

import com.koi151.msproperties.entity.payload.PaymentScheduleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyForRent {

    @Id
    private int property_id;

    @OneToOne
    @MapsId
    private Properties properties;

    @Column(name = "rental_price", nullable = false)
    @NotNull(message = "Rental price cannot be null")
    @PositiveOrZero(message = "Rental price must be positive or zero")
    private float rentalPrice;

    @Column(name = "payment_scheledule", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment schedule cannot be null")
    private PaymentScheduleEnum paymentSchedule;

    @Column(name = "rent_term", columnDefinition = "TEXT")
    private String rentTerm;
}
