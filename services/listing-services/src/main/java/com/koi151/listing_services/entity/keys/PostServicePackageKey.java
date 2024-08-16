package com.koi151.listing_services.entity.keys;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostServicePackageKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -9211148919685518506L;

    @Column(name = "property_service_package_id")
    private Long propertyServicePackageId;

    @Column(name = "post_service_id")
    private Long postServiceId;
}