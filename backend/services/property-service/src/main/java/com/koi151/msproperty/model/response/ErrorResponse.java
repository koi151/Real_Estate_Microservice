package com.koi151.msproperty.model.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String error;
    private List<String> details = new ArrayList<>();
}
