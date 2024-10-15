package com.koi151.msproperties.entity;

import com.koi151.msproperties.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;

@Entity(name="property_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1682566055866886258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(name = "title", nullable = false, length = 100)
    @NotBlank(message = "Property category title is mandatory")
    @Size(min = 5, max = 100, message = "Title length must be between {min} and {max} characters")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 2000, message = "Property category description cannot exceed 2000 characters")
    private String description;

    @Column(name = "images", columnDefinition = "TEXT")
    private String imageUrls;

//    @Column(name = "slug", length = 200)
//    @NotEmpty(message = "Property slug cannot be empty")
//    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusEnum status = StatusEnum.ACTIVE;
}