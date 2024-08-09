package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.PackageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "promotion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_service_id")
    private PostService postService;

    @Column(name = "package_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(name = "percent_discount", precision = 5, scale = 2)
    @Max(value = 100, message = "Discount percentage cannot exceed {max}%")
    private BigDecimal discountPercentage;

    @Column(name = "price_discount", precision = 10, scale = 2)
    @DecimalMax(value = "999999999.99", message = "Price discount cannot exceed {max}")
    private BigDecimal priceDiscount;
}












