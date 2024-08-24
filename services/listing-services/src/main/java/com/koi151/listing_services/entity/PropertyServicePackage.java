package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "property_service_package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyServicePackage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3009435991992635447L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyServicePackageId;

    @Column(name = "property_id") // from other service -> open feign
    private long propertyId;

    @OneToMany(mappedBy = "propertyServicePackage", cascade = CascadeType.ALL)
    private List<PostServicePackage> postServicePackages;

    @Column(name = "package_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Property service package type is mandatory")
    private PackageType packageType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "total_fee", precision = 10, scale = 2, nullable = false)
    @PositiveOrZero(message = "Total fee must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Total fee cannot exceed 99,999,999.99")
    private BigDecimal totalFee;
}