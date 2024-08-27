package com.koi151.payment.model.request;

import com.koi151.payment.enums.StatusEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentCreateRequest (

    @NotNull(message = "Property id is mandatory in payment")
    Long propertyId,

    @NotNull(message = "Total fee of payment is mandatory")
    @PositiveOrZero(message = "Total fee of payment must be non-negative value")
    @DecimalMax(value = "99999999.99", message = "Total fee of payment cannot exceed {value}")
    BigDecimal totalFee,

    String orderInfo,

    @NotBlank(message = "Bank code is mandatory")
    String bankCode,

    @NotBlank(message = "Transaction code is mandatory")
    String transactionNo,

    @NotNull(message = "Pay date is mandatory")
    LocalDateTime payDate,

    @NotNull(message = "Payment status is mandatory")
    StatusEnum status
) {}
