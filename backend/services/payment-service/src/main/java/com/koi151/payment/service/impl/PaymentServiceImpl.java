package com.koi151.payment.service.impl;

import com.koi151.payment.entity.Payment;
import com.koi151.payment.mapper.PaymentMapper;
import com.koi151.payment.model.dto.PaymentCreateDTO;
import com.koi151.payment.model.request.PaymentCreateRequest;
import com.koi151.payment.notification.PaymentNotificationRequest;
import com.koi151.payment.notification.NotificationProducer;
import com.koi151.payment.repository.PaymentRepository;
import com.koi151.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final NotificationProducer notificationProducer;

    @Override
    @Transactional
    public PaymentCreateDTO createPaymentFromSubmission (PaymentCreateRequest request) {
        // Payment create from Submission service data which is already validated
        Payment entity = paymentRepository.save(paymentMapper.toPaymentEntity(request));

        notificationProducer.sendNotification(
            PaymentNotificationRequest.builder()
                .referenceCode(request.referenceCode())
                .paymentMethod(request.paymentMethod())
                .totalFee(request.totalFee())
                .customerFirstName(request.customer().firstName())
                .customerLastName(request.customer().lastName())
                .customerEmail(request.customer().email())
                .build()
        );

        return paymentMapper.toPaymentCreateDTO(entity);
    }
}

