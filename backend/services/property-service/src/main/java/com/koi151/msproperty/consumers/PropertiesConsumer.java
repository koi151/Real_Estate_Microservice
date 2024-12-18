package com.koi151.msproperty.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperty.customExceptions.EntityNotFoundException;
import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.events.*;
import com.koi151.msproperty.messaging.PropertyStatusMessage;
import com.koi151.msproperty.repository.PropertyRepository;
import com.koi151.msproperty.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertiesConsumer {

    private final PropertyRepository propertyRepository;
    private final PropertyService propertyService;
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

    @KafkaListener(topics = "ServiceValidated", groupId = "properties_group")
    public void onPropertyPostServiceValid(String message) {
        try {
            ServiceValidatedEvent event = objectMapper.readValue(message, ServiceValidatedEvent.class);
            propertyService.updatePropertyStatus(event.getPropertyId(), StatusEnum.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "PaymentCompleted", groupId = "properties_group")
    public void onPaymentCompleted(String message) {
        try {
            PaymentCompletedEvent event = objectMapper.readValue(message, PaymentCompletedEvent.class);
            propertyService.updatePropertyStatus(event.getPropertyId(), StatusEnum.PENDING);
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
        propertyService.updatePropertyStatus(propertyId, StatusEnum.CANCELED);

        ListingCanceledEvent canceled = new ListingCanceledEvent(propertyId, accountId, reason);
        try {
            String json = objectMapper.writeValueAsString(canceled);
            kafkaTemplate.send("PropertyCanceled", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}