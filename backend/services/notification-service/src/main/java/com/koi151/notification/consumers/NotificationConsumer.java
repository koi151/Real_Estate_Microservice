package com.koi151.notification.consumers;

import com.koi151.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = {"PropertyCanceled"}, groupId = "notification_group")
    public void onPropertyCanceled(String message) {
        log.info("Received PropertyCanceled message: {}", message);
        notificationService.processPropertyCanceled(message);
    }


//    @KafkaListener(topics = {"ListingCanceled", "ListingPublished", "ListingRejected"}, groupId = "notification_group")
//    public void listen(String message) {
//        try {
//            JsonNode node = objectMapper.readTree(message);
//            Notification notification = Notification.builder()
//                .hasSeen(false)
//                .build();
//
//            if (node.has("reason") && node.has("propertyId")) {
//                // Canceled or Rejected
//                Long propertyId = node.get("propertyId").asLong();
//                String accountId = node.get("accountId").asText();
//                String reason = node.get("reason").asText();
//
//                if (message.contains("ListingRejected")) {
//                    notification.setNotificationType(NotificationType.REJECTED);
//                    SubmissionConfirmation sc = SubmissionConfirmation.builder()
//                        .referenceCode("") // test
//                        .totalAmount(BigDecimal.valueOf(0))
//                        .paymentMethod(PaymentMethod.PAYPAL)
//                        .customer(null)
//                        .propertyServicePackage(null)
//                        .build();
//
//                    notification.setSubmissionConfirmation(sc);
//                } else {
//                    notification.setNotificationType(NotificationType.CANCELED);
//                    // PaymentConfirmation or SubmissionConfirmation
//                    // suppose that reject bc payment fail || service fail => PaymentConfirmation fail
//                    PaymentConfirmation pc = PaymentConfirmation.builder()
//                        .referenceCode(null) // test
//                        .customerEmail(null)
//                        .customerFirstName(null)
//                        .customerLastName(null)
//                        .customerEmail(null)
//                        .build();
//
//                    notification.setPaymentConfirmation(pc);
//                }
//
//                System.out.println("Notify " + accountId + ": Listing " + propertyId + " failed: " + reason);
//
//            } else if (node.has("propertyId") && node.has("accountId") && !node.has("reason")) {
//                // Published
//                Long propertyId = node.get("propertyId").asLong();
//                String accountId = node.get("accountId").asText();
//
//                notification.setNotificationType(NotificationType.PUBLISHED);
//                // post publish, SubmissionConfirmation APPROVED
//                SubmissionConfirmation sc = SubmissionConfirmation.builder() // test
//                    .build();
//
//                notification.setSubmissionConfirmation(sc);
//
//                System.out.println("Notify " + accountId + ": Listing " + propertyId + " published successfully!");
//            }
//
//            notificationRepository.save(notification);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
