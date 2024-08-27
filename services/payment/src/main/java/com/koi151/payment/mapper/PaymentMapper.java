package com.koi151.payment.mapper;

import com.koi151.payment.entity.Payment;
import com.koi151.payment.model.dto.PaymentCreateDTO;
import com.koi151.payment.model.request.PaymentCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toPaymentEntity(PaymentCreateRequest request);

    @Mapping(target = "status", source = "status.statusName")
    PaymentCreateDTO toPaymentCreateDTO(Payment entity);
}
