package com.koi151.property_submissions.model.response;

public record CustomerResponse (
    Long accountId,
    String accountName,
    String firstName,
    String lastName,
    String email
) {}
