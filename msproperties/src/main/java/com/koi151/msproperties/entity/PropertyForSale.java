package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyForSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propertyId;

    @Column(name = "sale_price")
    private float salePrice;

    @Column(name = "property_status", nullable = false, length = 20)
    private String propertyStatus;

    @Column(name = "payment_schedule", nullable = false, length = 20)
    private String paymentSchedule;

    @Column(name = "sale_term")
    private String saleTerm;

}
