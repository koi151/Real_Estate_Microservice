package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.PackageType;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "post_service_pricing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostServicePricing extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -6091883146945983588L;

    @Id
    private Long postServiceId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_service_id")
    private PostService postService;

    @Column(name = "package_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(name = "price", precision = 12, scale = 2)
    @NotNull(message = "Post service pricing cannot be empty")
    @PositiveOrZero(message = "Post service pricing must be non-negative value")
    @DecimalMax(value = "9999999999.99", message = "Post service pricing cannot exceed 9,999,999,999.99")
    private BigDecimal price;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP(0)", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime endDate;

//    @PrePersist // method is automatically invoked by the JPA provider (Hibernate) just before the entity is persisted
//    public void prePersist() {
//        if (startDate == null) {
//            startDate = LocalDateTime.now(); // default start date;
//        }
//    }
}