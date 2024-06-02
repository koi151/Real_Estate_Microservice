package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyForSale {

    @Id
    private int propertyId;

    @OneToOne
    @MapsId
    private Properties properties;

    @Column(name = "sale_price")
    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Price must be positive or zero")
    private float salePrice;

    @Column(name = "sale_term", columnDefinition = "TEXT")
    private String saleTerm;
}
