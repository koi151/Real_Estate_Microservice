package com.koi151.payment.service.impl;

import com.koi151.payment.entity.Payment;
import com.koi151.payment.events.ServiceValidatedEvent;
import com.koi151.payment.mapper.PaymentMapper;
import com.koi151.payment.model.dto.PaymentCreateDTO;
import com.koi151.payment.model.request.PaymentCreateRequest;
import com.koi151.payment.notification.PaymentNotificationRequest;
import com.koi151.payment.notification.NotificationProducer;
import com.koi151.payment.repository.PaymentRepository;
import com.koi151.payment.service.PaymentService;
import com.koi151.payment.utils.VNPayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final NotificationProducer notificationProducer;

    @Value("${VNP_TMN_CODE}")
    private String vnpTmnCode;

    @Value("${VNP_SECRET_KEY}")
    private String vnpSecretKey;

    @Value("${VNP_PAYURL}")
    private String vnpPayUrl;

    @Value("${VNP_RETURN_URL}")
    private String vnpReturnUrl;

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

    public String createVNPayPaymentUrl(ServiceValidatedEvent event) {
        Map<String, String> vnp_Params = new HashMap<>();
        String propertyId = event.getPropertyId().toString();

        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((long)(event.getTotalAmount() * 100))); // amount in VND * 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", propertyId);
        vnp_Params.put("vnp_OrderInfo", "Payment for property ID: " + propertyId);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1"); // mock IP address
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        // Sort and build query
        String query = VNPayUtils.buildQuery(vnp_Params);
        String secureHash = VNPayUtils.hmacSHA512(vnpSecretKey, query);
        String paymentUrl = vnpPayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;

        log.info("VNPay Payment URL: {}", paymentUrl);
        return paymentUrl;
    }

    @Override
    public boolean processPayment(ServiceValidatedEvent event) {
        var abc = "";

        return false;
    }

}

