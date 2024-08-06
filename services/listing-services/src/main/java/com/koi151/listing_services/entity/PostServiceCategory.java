package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;

@Entity(name = "post_service_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostServiceCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1471874958387191022L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postServiceCategoryId;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Post service category name is mandatory")
    @Size(min = 6, max = 100, message = "Post service category name must between {min} and {max} characters")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "description")
    @Size(max = 2000, message = "Post service category description cannot exceed {max} characters")
    private String description;
}






















