package com.koi151.payment.model.dto;

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
    private String referenceCode;
    private LocalDate payDate;
    private String status;
}
