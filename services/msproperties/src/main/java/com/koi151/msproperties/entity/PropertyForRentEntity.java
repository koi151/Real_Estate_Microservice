package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.PaymentScheduleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(name = "rental_price", precision = 12, scale = 2, nullable = false)
    @NotNull(message = "Rental price is mandatory")
    @PositiveOrZero(message = "Rental price must be non-negative value")
    @DecimalMax(value = "99999999999", message = "Rental price cannot exceed 99,999,999,999")
    private BigDecimal rentalPrice;

    @Column(name = "payment_schedule", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment schedule is mandatory")
    private PaymentScheduleEnum paymentSchedule;

    @Column(name = "rental_terms", columnDefinition = "TEXT")
    @Size(max = 5000, message = "Rental terms cannot exceed {max} characters")
    private String rentalTerms;
}
