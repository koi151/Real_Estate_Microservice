package com.koi151.notification.entity;

import com.koi151.notification.enums.NotificationType;
import lombok.*;
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
    private String id;
    private NotificationType notificationType;
    private boolean hasSeen;
}










