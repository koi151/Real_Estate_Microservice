package com.koi151.payment.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private String error;
    private List<String> details = new ArrayList<>();
}
