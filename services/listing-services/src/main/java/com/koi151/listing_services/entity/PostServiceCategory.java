package com.koi151.listing_services.entity;

import com.koi151.listing_services.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.util.List;

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

    @OneToMany(mappedBy = "postServiceCategory", fetch = FetchType.LAZY)
    private List<PostService> postService;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Post service category name is mandatory")
    @Size(min = 6, max = 100, message = "Post service category name must between {min} and {max} characters")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "description", length = 2000)
    @Size(max = 2000, message = "Post service category description cannot exceed {max} characters")
    private String description;
}






















