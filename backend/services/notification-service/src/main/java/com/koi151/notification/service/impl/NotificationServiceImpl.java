package com.koi151.notification.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.notification.customExceptions.JsonProcessingException;
import com.koi151.notification.entity.Notification;
import com.koi151.notification.enums.NotificationType;
import com.koi151.notification.enums.SenderType;
import com.koi151.notification.repository.NotificationRepository;
import com.koi151.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public void processPropertyCanceled(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);

            String recipientId = node.has("accountId") ? node.get("accountId").asText() : null;
            String propertyId = node.has("propertyId") ? node.get("propertyId").asText() : null;
            String reason = node.has("reason") ? node.get("reason").asText() : "No information";

            Notification notification = Notification.builder()
                .notificationType(NotificationType.PROPERTY_POST)
                .senderType(SenderType.SYSTEM)
                .recipientId(recipientId)
                .hasSeen(false)
                .title("Property Post Cancelled")
                .details("Property ID: " + propertyId + ", Reason: " + reason)
                .build();

            notificationRepository.save(notification);

        } catch (Exception e) {
            throw new JsonProcessingException(e.getMessage());
        }
    }

}
