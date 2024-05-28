package com.koi151.mspropertycategory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponseDTO {
    private String error;
    private List<String> detail = new ArrayList<>();
}
