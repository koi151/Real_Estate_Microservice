package com.koi151.msproperties.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponseDTO {

    private String error;
    private List<String> details = new ArrayList<>();
}
