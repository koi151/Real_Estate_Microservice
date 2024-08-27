package com.koi151.payment.model.dto;

import com.koi151.payment.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateDTO {
    private Long paymentId;
    private Long propertyId;
    private BigDecimal totalFee;
    private String orderInfo;
    private String bankCode;
    private String transactionNo;
    private LocalDate payDate;
    private String status;
}
