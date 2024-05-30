package com.koi151.msproperties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category id cannot be null")
    @Positive(message = "Category id must be positive")
    private int categoryId;

    @Column(name = "available_from", nullable = false)
    @NotEmpty(message = "Available time cannot be empty")
    private String availableFrom;

    @Column(name = "title", nullable = false)
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Column(name = "price", nullable = false)
    @NotNull(message = "Price cannot be empty")
    @PositiveOrZero(message = "Price must be positive or zero")
    private float price;

    @Column(name = "area", nullable = false)
    @NotNull(message = "Area cannot be empty")
    @PositiveOrZero(message = "Area must be positive or zero")
    private float area;

    @Column(name = "description")
    private String description;

    @Column(name = "images")
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
    private String status = "ACTIVE";

    @Enumerated(EnumType.STRING)
    @Column(name = "house_direction", length = 20)
    private Direction houseDirection;

    @Column(name = "balcony_direction", length = 20)
    private Direction balconyDirection;

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
        this.price = properties.price;
        this.status = properties.status;
        this.houseDirection = properties.houseDirection;
        this.balconyDirection = properties.balconyDirection;
        this.deleted = properties.deleted;
        this.createdAt = properties.createdAt;
        this.updatedAt = properties.updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
