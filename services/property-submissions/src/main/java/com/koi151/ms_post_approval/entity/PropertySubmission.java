package com.koi151.ms_post_approval.entity;

import com.koi151.ms_post_approval.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;

@Entity(name = "property_submission")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertySubmission extends BaseEntity{

    @Serial
    private static final long serialVersionUID = -2316137754949864655L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertySubmissionId;

    @Column(name = "property_id", nullable = false, unique = true)
    @NotNull(message = "Property id cannot be null")
    @Positive(message = "Property id must be positive value")
    private Long propertyId;

    @Column(name = "account_id", nullable = false)
    @NotNull(message = "Account id cannot be null")
    @Positive(message = "Account id must be positive value")
    private Long accountId;

    @Column(name = "reference_code", length = 100, nullable = false, unique = true)
    @NotBlank(message = "Reference code cannot be blank")
    @Size(max = 100, message = "Reference code cannot exceed 100 characters")
    private String referenceCode;

    @Column(name = "reviewer_id")
    @Positive(message = "Reviewer id must be positive value")
    private Long reviewerId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;;

    @Column(name = "review_message", length = 3000)
    @Size(max = 3000, message = "Review message cannot exceed 3000 characters")
    private String reviewMessage;
}