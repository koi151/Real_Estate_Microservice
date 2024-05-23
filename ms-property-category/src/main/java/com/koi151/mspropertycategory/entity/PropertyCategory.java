package com.koi151.mspropertycategory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "desc")
    private String desc;

    @Column(name = "images")
    private String images;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "deleted", columnDefinition = "boolean default true")
    private boolean deleted;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

}
