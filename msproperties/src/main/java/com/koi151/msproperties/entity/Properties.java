package com.koi151.msproperties.entity;

import jakarta.persistence.*;
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

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "available_from", nullable = false)
    private String availabeFrom;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "area", nullable = false)
    private float area;

    @Column(name = "description")
    private String description;

    @Column(name = "images")
    private String imageUrls;

    @Column(name = "view")
    private int view;

    @Column(name = "total_floor")
    private int totalFloor;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "active";

    @Column(name = "house_direction", length = 20)
    private String houseDirection;

    @Column(name = "balcony_direction", length = 20)
    private String balconyDirection;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    public Properties(Properties properties) {
        this.id = properties.id;
        this.categoryId = properties.categoryId;
        this.availabeFrom = properties.availabeFrom;
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
