package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "property_for_sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyForSaleEntity {

    @Id
    private Long propertyId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "sale_price")
    @NotNull(message = "Sale price is mandatory")
    @PositiveOrZero(message = "Price must be non-negative value")
    @DecimalMax(value = "99_999_999_999", message = "Rental price cannot exceed 99,999,999,999")
    private BigDecimal salePrice;

    @Column(name = "sale_terms", columnDefinition = "TEXT")
    @Size(max = 5000, message = "Sale terms cannot exceed {max} characters")
    private String saleTerms;
}
