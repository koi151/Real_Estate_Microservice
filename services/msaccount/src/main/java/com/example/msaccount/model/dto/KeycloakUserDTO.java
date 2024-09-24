package com.example.msaccount.model.dto;

import java.util.List;

public record KeycloakUserDTO (
    String id,
    String username,
    List<String> roleNames,
    String firstName,
    String lastName,
    String email
) {}
