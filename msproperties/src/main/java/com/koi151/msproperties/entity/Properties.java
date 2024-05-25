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

    @Column(name = "available_from", nullable = false)
    private LocalDateTime availabeFrom;

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

    @Column(name = "post_position")
    private int postPosition;

    // slug

    @Column(name = "house_direction", length = 20)
    private String houseDirection;

    @Column(name = "balcony_direction", length = 20)
    private String balconyDirection;

    @Column(name = "deleted", nullable = false, length = 10)
    private boolean deleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @ManyToOne
//    @JoinColumn(name = "propertyCategory")
//    private PropertyCategory propertyCategory;
}
