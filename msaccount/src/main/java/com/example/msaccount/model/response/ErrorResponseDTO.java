package com.example.msaccount.model.response;

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
