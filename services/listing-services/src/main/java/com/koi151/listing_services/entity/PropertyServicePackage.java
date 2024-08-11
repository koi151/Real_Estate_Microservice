package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.util.List;

@Entity(name = "property_post_service")
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
    private Long propertyServiceId;

    @ManyToOne
    @JoinColumn(name = "post_service_id")
    private PostService postService;

    @OneToMany(mappedBy = "propertyServicePackage", cascade = CascadeType.ALL)
    private List<PostServicePackage> postServicePackages;

    @Column(name = "package_type")
    @NotNull(message = "Property service package type is mandatory")
    private PackageType packageType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
















