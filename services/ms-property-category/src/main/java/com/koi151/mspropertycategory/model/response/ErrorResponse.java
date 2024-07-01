package com.koi151.mspropertycategory.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private List<String> details = new ArrayList<>();
}
