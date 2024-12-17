package com.koi151.notification.entity;

import com.koi151.notification.enums.NotificationType;
import com.koi151.notification.enums.SenderType;
import com.koi151.notification.kafka.payment.PaymentConfirmation;
import com.koi151.notification.kafka.submission.PropertyServicePackage;
import com.koi151.notification.kafka.submission.SubmissionConfirmation;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document // used to mark a Java class as a MongoDB document
public class Notification extends BaseEntity {

    @Id
    private ObjectId id;
    private NotificationType notificationType;
    private SenderType senderType;
    private boolean hasSeen;
    private String recipientId;
    private String senderId;
    private String title;
    private String details;
}










