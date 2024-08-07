package com.koi151.listing_services.entity;

import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "property_service_id")
    PropertyServicePackage propertyServicePackage;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "post_service_id")
    PostService postService;
}
