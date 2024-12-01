package com.example.msaccount.model.dto;


import lombok.Builder;

@Builder
public record CookieAttributes (
    String name,
    String value,
    int maxAge
) {}
