package com.example.msaccount.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record KeycloakUserDTO (
    String id,
    String username,
    List<String> roleNames,
    String firstName,
    String lastName,
    String email
) {}
