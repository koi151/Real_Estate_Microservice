package com.koi151.mspropertycategory.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData {

    private int status = 200;
    private boolean isSuccess = true;
    private String desc;
    private Object data;
}
