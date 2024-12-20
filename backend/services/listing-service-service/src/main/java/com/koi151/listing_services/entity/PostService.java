package com.koi151.listing_services.entity;


import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "post_service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postServiceId;

    @OneToMany(mappedBy = "postService", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<PostServicePricing> postServicePricings;

    @OneToMany(mappedBy = "postService",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PostServicePackage> postServicePackage;

    @OneToMany(mappedBy = "postService", cascade = CascadeType.ALL)
    private List<Promotion> promotions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_service_category_id")
    private PostServiceCategory postServiceCategory;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotNull(message = "Post service name is mandatory")
    @Size(min = 5, max = 100, message = "Post service name must between {min} and {max} characters")
    private String name;

    @Column(name = "available_units", nullable = false)
    @NotNull(message = "Post service available unit quantity is mandatory")
    @Positive(message = "Post service available unit quantity must greater than zero")
    private int availableUnits;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "description", length = 2000)
    @Size(max = 2000, message = "Post service description cannot exceed {max} characters")
    private String description;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}