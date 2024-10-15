package com.koi151.property_submissions.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionProducer {

    private final KafkaTemplate<String, SubmissionConfirmation> kafkaTemplate;

    public void sendSubmissionConfirmation(SubmissionConfirmation submissionConfirmation) {
        log.info("sending property submission confirmation");
        Message<SubmissionConfirmation> message = MessageBuilder
            .withPayload(submissionConfirmation)
            .setHeader(KafkaHeaders.TOPIC, "submission-topic")
            .build();
        kafkaTemplate.send(message);
    }

}