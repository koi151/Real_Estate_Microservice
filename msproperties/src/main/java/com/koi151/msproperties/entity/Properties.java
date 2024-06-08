package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "properties", cascade = CascadeType.ALL)
    private PropertyForSale propertyForSale;

    @OneToOne(mappedBy = "properties", cascade = CascadeType.ALL)
    private PropertyForRent propertyForRent;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    @NotNull(message = "Address id cannot be null")
    private Address address;

    @OneToMany(mappedBy = "properties")
    private Set<Room> roomSet;

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
    private StatusEnum statusEnum = StatusEnum.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_direction", length = 20)
    private DirectionEnum houseDirectionEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "balcony_direction", length = 20)
    private DirectionEnum balconyDirectionEnum;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    public Properties(Properties properties) {
        this.id = properties.id;
        this.categoryId = properties.categoryId;
        this.availableFrom = properties.availableFrom;
        this.title = properties.title;
        this.area = properties.area;
        this.description = properties.description;
        this.imageUrls = properties.imageUrls;
        this.view = properties.view;
        this.totalFloor = properties.totalFloor;
        this.statusEnum = properties.statusEnum;
        this.houseDirectionEnum = properties.houseDirectionEnum;
        this.balconyDirectionEnum = properties.balconyDirectionEnum;
        this.deleted = properties.deleted;
        this.createdAt = properties.createdAt;
        this.updatedAt = properties.updatedAt;
        this.roomSet = properties.roomSet;
        this.propertyForSale = properties.propertyForSale;  // !
        this.propertyForRent = properties.propertyForRent;
        this.address = properties.address;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
