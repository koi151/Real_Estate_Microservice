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

    @Column(name = "area", nullable = false)
    private float area;

    @Column(name = "description")
    private String description;

    @Column(name = "images")
    private String images;

    @Column(name = "view")
    private int view;

    @Column(name = "total_floor")
    private int totalFloor;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "active";

    // slug

    @Column(name = "house_direction", length = 20)
    private String houseDirection;

    @Column(name = "balcony_direction", length = 20)
    private String balconyDirection;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "propertyCategory")
//    private PropertyCategory propertyCategory;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
