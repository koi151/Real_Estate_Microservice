package com.example.msaccount.consumers;

import com.example.msaccount.events.PaymentCompletedEvent;
import com.example.msaccount.events.PaymentFailedEvent;
import com.example.msaccount.events.ServiceValidatedEvent;
import com.example.msaccount.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountConsumer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccountService accountService;

    @KafkaListener(topics = "ServiceValidated", groupId = "payment_group")
    public void onServiceValidated(String message) {
        try {
            ServiceValidatedEvent event = objectMapper.readValue(message, ServiceValidatedEvent.class);
            boolean success = accountService.updateAccountBalance(event);

            if (success) {
                PaymentCompletedEvent completed = new PaymentCompletedEvent(event.getPropertyId(), event.getAccountId());
                kafkaTemplate.send("PaymentCompleted", objectMapper.writeValueAsString(completed));
            } else {
                PaymentFailedEvent failed = new PaymentFailedEvent(event.getPropertyId(), event.getAccountId(), "Payment failed");
                kafkaTemplate.send("PaymentFailed", objectMapper.writeValueAsString(failed));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

