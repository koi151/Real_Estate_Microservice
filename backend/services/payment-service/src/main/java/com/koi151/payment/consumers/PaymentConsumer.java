//package com.koi151.payment.consumers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.koi151.payment.events.PaymentCompletedEvent;
//import com.koi151.payment.events.PaymentFailedEvent;
//import com.koi151.payment.events.PaymentRequestedEvent;
//import com.koi151.payment.events.ServiceValidatedEvent;
//import com.koi151.payment.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentConsumer {
//    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final PaymentService paymentService;
//}
