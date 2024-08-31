package com.koi151.payment.notification;

import com.koi151.payment.model.request.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationProducer {

    // KafkaTemplate provide methods for sending messages to Kafka
    private final KafkaTemplate<String, PaymentNotificationRequest>  kafkaTemplate;

    public void sendNotification(PaymentNotificationRequest request) {
        log.info("Sending notification with body = < {} >", request);
        Message<PaymentNotificationRequest> message = MessageBuilder // create Kafka Message
            .withPayload(request)
            .setHeader(KafkaHeaders.TOPIC, "payment-topic")
            .build();

        kafkaTemplate.send(message);
    }
}
