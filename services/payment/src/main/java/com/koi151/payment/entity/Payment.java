package com.koi151.payment.entity;

import com.koi151.payment.enums.PaymentMethod;
import com.koi151.payment.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 462795159781824903L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "property_id", nullable = false)
    @NotNull(message = "Property id is mandatory in payment")
    private Long propertyId;

    @Column(name = "total_fee", precision = 12, scale = 2, nullable = false)
    @NotNull(message = "Total fee in payment is mandatory")
    @PositiveOrZero(message = "Payment amount must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Total fee of payment cannot exceed {value}")
    private BigDecimal totalFee;

    @Column(name = "payment_method", nullable = false)
    @NotNull(message = "Payment method is mandatory")
    private PaymentMethod paymentMethod;

    @Column(name = "order_info", columnDefinition = "TEXT")
    private String orderInfo;

    @Column(name = "bank_code", length = 30, nullable = false)
    @NotBlank(message = "Bank code is mandatory")
    private String bankCode;

    @Column(name = "transaction_no", length = 50, nullable = false)
    @NotBlank(message = "Transaction is mandatory")
    private String transactionNo;

    @Column(name = "pay_date", nullable = false)
    @NotNull(message = "Pay date is mandatory")
    private LocalDate payDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}

















