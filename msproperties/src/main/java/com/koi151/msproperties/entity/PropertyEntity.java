package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "propertyEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForSaleEntity propertyForSaleEntity;

    @OneToOne(mappedBy = "propertyEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private PropertyForRentEntity propertyForRentEntity;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    @NotNull(message = "Address id cannot be null")
    private AddressEntity addressEntity;

    @OneToMany(mappedBy = "propertyEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}
                                                                                            , orphanRemoval = true)
    private Set<RoomEntity> roomEntities;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category id cannot be null")
    @Positive(message = "Category id must be positive")
    private int categoryId;

    @Column(name = "available_from", nullable = false, length = 30)
    @NotEmpty(message = "Available time cannot be empty")
    @Size(max = 30, message = "Available time cannot longer than 30 characters")
    private String availableFrom;

    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Column(name = "area", nullable = false)
    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private float area;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "images", columnDefinition = "TEXT")
    private String imageUrls;

    @Column(name = "view", nullable = false)
    @NotNull(message = "Number of view cannot be null")
    @PositiveOrZero(message = "View number must be positive or zero")
    private int view = 0;

    @Column(name = "total_floor")
    @PositiveOrZero(message = "Total floor must be positive or zero")
    private int totalFloor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusEnum status = StatusEnum.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_direction", length = 20)
    private DirectionEnum houseDirection;

    @Enumerated(EnumType.STRING)
    @Column(name = "balcony_direction", length = 20)
    private DirectionEnum balconyDirection;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

//    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

//    @PrePersist
//    protected void onCreate() {
//        if (createdAt == null) {
//            createdAt = LocalDateTime.now();
//        }
//    }
}