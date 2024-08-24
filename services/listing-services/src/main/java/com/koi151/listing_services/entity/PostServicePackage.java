package com.koi151.listing_services.entity;

import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "post_service_package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostServicePackage {

    @EmbeddedId
    PostServicePackageKey postServicePackageKey;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "property_service_package_id", insertable = false, updatable = false)
    PropertyServicePackage propertyServicePackage;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "post_service_id", insertable = false, updatable = false)
    PostService postService;

    @Column(name = "units_remaining", nullable = false)
    @NotNull(message = "Units remaining quantity is mandatory")
    @Positive(message = "Units remaining quantity must be greater than zero")
    private int unitsRemaining;

    @Column(name = "price_at_creation", columnDefinition = "DECIMAL(9,2) DEFAULT 0.00")
    @PositiveOrZero(message = "Post service price must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Post service price cannot exceed {value}")
    private BigDecimal priceAtCreation;

    @Column(name = "discount_percentage_at_creation", columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
    @PositiveOrZero(message = "Discount percentage must be non-negative value")
    @DecimalMax(value = "99.99", message = "Post service price cannot exceed {value}")
    private BigDecimal discountPercentageAtCreation;

    @Column(name = "price_discount_at_creation", columnDefinition = "DECIMAL(9,2) DEFAULT 0.00")
    @PositiveOrZero(message = "Price discount must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Price discount cannot exceed {value}")
    private BigDecimal priceDiscountAtCreation;
}











