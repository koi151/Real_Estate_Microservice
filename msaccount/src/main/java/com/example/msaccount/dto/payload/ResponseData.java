package com.example.msaccount.dto.payload;

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
