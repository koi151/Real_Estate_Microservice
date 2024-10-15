package com.koi151.property_submissions.model.response;

public record CustomerResponse (
    Long accountId,
    String firstName,
    String lastName,
    String email
) {}
