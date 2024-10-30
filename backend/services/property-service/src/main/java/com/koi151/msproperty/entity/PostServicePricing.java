package com.koi151.msproperty.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostServicePricing extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4393043598135598711L;

    @MapsId
    private long postServiceId;

    @Column(name = "posting_package_price", precision = 12, scale = 2)
    @PositiveOrZero(message = "Posting package price must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private BigDecimal postingPackagePrice;

    @Column(name = "priority_pushes_total_price", precision = 12, scale = 2)
    @PositiveOrZero(message = "Priority pushes total price must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private BigDecimal priorityPushesTotalPrice;

    @Column(name = "days_posted_price", precision = 12, scale = 2)
    @PositiveOrZero(message = "Priority pushes total price must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private BigDecimal daysPostedPrice;

    @Column(name = "total_price", precision = 12, scale = 2)
    @PositiveOrZero(message = "Priority pushes total price must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Area cannot exceed 99,999,999.99")
    private BigDecimal totalPrice;
}
