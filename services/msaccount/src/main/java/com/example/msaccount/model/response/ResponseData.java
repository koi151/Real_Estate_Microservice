package com.example.msaccount.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {
    private short status = 200;
    private Object data;
    private String desc;
    private boolean isSuccess = true;
}
