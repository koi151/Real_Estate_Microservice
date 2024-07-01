package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity(name = "property_for_sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyForSaleEntity {

    @Id
    private Long property_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "sale_price")
    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Price must be positive or zero")
    private float salePrice;

    @Column(name = "sale_term", columnDefinition = "TEXT")
    private String saleTerm;
}
