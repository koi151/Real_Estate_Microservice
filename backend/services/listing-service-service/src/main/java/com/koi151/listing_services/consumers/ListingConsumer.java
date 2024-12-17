package com.koi151.listing_services.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.events.ListingCreatedEvent;
import com.koi151.listing_services.events.ServiceValidatedEvent;
import com.koi151.listing_services.events.ServiceValidationFailedEvent;
import com.koi151.listing_services.mapper.PropertyServicePackageMapper;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.service.PropertyServicePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingConsumer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PropertyServicePackageService propertyServicePackageService;
    private final PropertyServicePackageMapper propertyServicePackageMapper;
    @KafkaListener(topics = "ListingCreated", groupId = "listing_group")
    public void listenListingCreated(String message) {
        ListingCreatedEvent event = null;
        try {
            event = objectMapper.readValue(message, ListingCreatedEvent.class);
            PropertyServicePackageCreateRequest request = propertyServicePackageMapper.toPropertyServicePackageCreateRequest(event);
            PropertyServicePackageCreateDTO dto = propertyServicePackageService.createPropertyServicePackage(request);

            ServiceValidatedEvent validated = ServiceValidatedEvent.builder()
                .propertyId(event.getPropertyId())
                .totalFee(dto.getTotalFee())
                .build();

            kafkaTemplate.send("ServiceValidated", objectMapper.writeValueAsString(validated));

        } catch (Exception e) {
            if (event != null) {
                try {
                    ServiceValidationFailedEvent failed = ServiceValidationFailedEvent.builder()
                        .propertyId(event.getPropertyId())
                        .reason("Invalid services")
                        .build();

                    kafkaTemplate.send("ServiceValidationFailed", objectMapper.writeValueAsString(failed));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
}
