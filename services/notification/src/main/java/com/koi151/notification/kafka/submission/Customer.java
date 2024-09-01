package com.koi151.notification.kafka.submission;

public record Customer (
    Long accountId,
    String firstName,
    String lastName,
    String email
) {}
