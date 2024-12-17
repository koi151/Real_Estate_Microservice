//package com.koi151.notification.kafka;
//
//import com.koi151.notification.email.EmailService;
//import com.koi151.notification.entity.Notification;
//import com.koi151.notification.enums.NotificationType;
//import com.koi151.notification.kafka.payment.PaymentConfirmation;
//import com.koi151.notification.kafka.submission.SubmissionConfirmation;
//import com.koi151.notification.repository.NotificationRepository;
//import jakarta.mail.MessagingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class NotificationConsumer {
//
//    private final NotificationRepository notificationRepository;
//    private final EmailService emailService;
//
//    @KafkaListener(topics = "payment-topic")
//    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
//        log.info(String.format("Consuming the message from payment-topic Topic::%s", paymentConfirmation));
//        notificationRepository.save(Notification.builder()
//            .notificationType(NotificationType.PAYMENT)
//            .hasSeen(false)
//            .paymentConfirmation(paymentConfirmation)
//            .build());
//
//        String customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
//        emailService.sendPaymentSuccessfulEmail(
//            paymentConfirmation.customerEmail(),
//            customerName,
//            paymentConfirmation.totalFee(),
//            paymentConfirmation.referenceCode()
//        );
//    }
//
//    @KafkaListener(topics = "submission-topic")
//    public void consumeSubmissionSuccessNotification(SubmissionConfirmation submissionConfirmation) throws MessagingException {
//        log.info(String.format("Consuming the message from submission-topic Topic::%s", submissionConfirmation));
//        notificationRepository.save(Notification.builder()
//            .notificationType(NotificationType.SUBMISSION)
//            .hasSeen(false)
//            .submissionConfirmation(submissionConfirmation)
//            .build());
//
//        String customerName = submissionConfirmation.customer().firstName() + " "
//                + submissionConfirmation.customer().lastName();
//        emailService.sendSubmissionConfirmationEmail(
//            submissionConfirmation.customer().email(),
//            submissionConfirmation.referenceCode(),
//            submissionConfirmation.paymentMethod(),
//            customerName,
//            submissionConfirmation.propertyServicePackage()
//        );
//    }
//}
//
//
//
//
//
