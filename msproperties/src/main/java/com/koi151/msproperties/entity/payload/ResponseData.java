package com.koi151.msproperties.entity.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {
    private Object data;
    private String desc;
    private int status = 200;
    private boolean isSuccess = true;
}
