package com.koi151.listing_services.entity;


import com.ctc.wstx.shaded.msv_core.verifier.ValidationUnrecoverableException;
import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "post_service_id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long post_service_id;

    @OneToOne(mappedBy = "postService", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private PostServicePricing postServicePricing;

    @OneToMany(mappedBy = "postService",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PostServicePackage> postServicePackage;

    @OneToMany(mappedBy = "postService", cascade = CascadeType.ALL)
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "postService")
    private List<PropertyServicePackage> propertyServicePackages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_service_category_id")
    private PostServiceCategory postServiceCategoryId;

    @Column(name = "post_service_category_id", nullable = false, unique = true, length = 100)
    @NotNull(message = "Post service category id is mandatory")
    @Size(min = 5, max = 100, message = "Post service name must between {min} and {max} characters")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @Column(name = "description", length = 2000)
    @Size(max = 2000, message = "Post service description cannot exceed {max} characters")
    private String description;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

}










