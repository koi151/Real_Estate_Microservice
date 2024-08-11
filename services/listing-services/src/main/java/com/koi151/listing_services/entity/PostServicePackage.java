package com.koi151.listing_services.entity;

import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "property_service_id", insertable = false, updatable = false)
    PropertyServicePackage propertyServicePackage;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "post_service_id", insertable = false, updatable = false)
    PostService postService;

    @Column(name = "units_remaining", nullable = false)
    @NotNull(message = "Units remaining quantity is mandatory")
    @Positive(message = "Units remaining quantity must be greater than zero")
    private int unitsRemaining;

}
