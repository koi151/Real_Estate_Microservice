package com.koi151.notification.email;

import com.koi151.notification.enums.EmailTemplates;
import com.koi151.notification.enums.PaymentMethod;
import com.koi151.notification.kafka.submission.PropertyServicePackage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.koi151.notification.enums.EmailTemplates.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private void sendEmail(
            String destinationEmail,
            EmailTemplates emailTemplate,
            Map<String, Object> variables
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("koi151tech@gmail.com");
        final String templateName = emailTemplate.getTemplate();

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(emailTemplate.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);

            mailSender.send((mimeMessage));
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException ex) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }

    @Async
    public void sendPaymentSuccessfulEmail(
        String destinationEmail,
        String customerName,
        BigDecimal totalFee,
        String referenceCode
    ) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalFee", totalFee);
        variables.put("referenceCode", referenceCode);

        sendEmail(destinationEmail, PAYMENT_CONFIRMATION, variables);
    }

    @Async
    public void sendSubmissionConfirmationEmail(
        String destinationEmail,
        String referenceCode,
        PaymentMethod paymentMethod,
        String customerName,
        PropertyServicePackage propertyServicePackage
    ) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("paymentMethod", paymentMethod);
        variables.put("customerName", customerName);
        variables.put("referenceCode", referenceCode);
        variables.put("propertyServicePackage", propertyServicePackage);

        log.info("propertyServicePackage: " + propertyServicePackage);
        sendEmail(destinationEmail, SUBMISSION_CONFIRMATION, variables);
    }
}
