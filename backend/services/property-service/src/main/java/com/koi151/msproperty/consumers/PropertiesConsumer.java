package com.koi151.msproperty.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.events.*;
import com.koi151.msproperty.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertiesConsumer {

    private final PropertyRepository propertyRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "ListingApproved", groupId = "properties_group")
    public void onListingApproved(String message) {
        try {
            ListingApprovedEvent event = objectMapper.readValue(message, ListingApprovedEvent.class);
            Optional<Property> propOpt = propertyRepository.findById(event.getPropertyId());
            if (propOpt.isPresent()) {
                Property property = propOpt.get();
                property.setStatus(StatusEnum.ACTIVE);
                propertyRepository.save(property);

                ListingPublishedEvent published = new ListingPublishedEvent(event.getPropertyId(), event.getAccountId());
                String json = objectMapper.writeValueAsString(published);
                kafkaTemplate.send("ListingPublished", json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "ListingRejected", groupId = "properties_group")
    public void onListingRejected(String message) {
        try {
            ListingRejectedEvent event = objectMapper.readValue(message, ListingRejectedEvent.class);
            cancelProperty(event.getPropertyId(), event.getAccountId(), "Rejected: " + event.getReason());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "ServiceValidationFailed", groupId = "properties_group")
    public void onServiceValidationFailed(String message) {
        try {
            ServiceValidationFailedEvent event = objectMapper.readValue(message, ServiceValidationFailedEvent.class);
            cancelProperty(event.getPropertyId(), event.getAccountId(), "Service validation failed: " + event.getReason());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "PaymentFailed", groupId = "properties_group")
    public void onPaymentFailed(String message) {
        try {
            PaymentFailedEvent event = objectMapper.readValue(message, PaymentFailedEvent.class);
            cancelProperty(event.getPropertyId(), event.getAccountId(), "Payment failed: " + event.getReason());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelProperty(Long propertyId, String accountId, String reason) {
        Optional<Property> propOpt = propertyRepository.findById(propertyId);

        if (propOpt.isPresent()) {
            Property propertyEntity = propOpt.get();
            propertyEntity.setStatus(StatusEnum.PENDING);
            propertyRepository.save(propertyEntity);

            ListingCanceledEvent canceled = new ListingCanceledEvent(propertyId, accountId, reason);
            try {
                String json = objectMapper.writeValueAsString(canceled);
                kafkaTemplate.send("PropertyCanceled", json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}