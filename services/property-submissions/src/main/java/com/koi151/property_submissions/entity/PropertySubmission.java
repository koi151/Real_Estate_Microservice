package com.koi151.property_submissions.entity;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

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
    @Size(max = 100, message = "Reference code cannot exceed {max} characters")
    private String referenceCode;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;;

    @Column(name = "review_message", length = 3000)
    @Size(max = 3000, message = "Review message cannot exceed {max} characters")
    private String reviewMessage;

    @Column(name = "total_fee", precision = 10, scale = 2, nullable = false)
    @PositiveOrZero(message = "Total fee must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Total fee cannot exceed 99,999,999.99")
    private BigDecimal totalFee;
}