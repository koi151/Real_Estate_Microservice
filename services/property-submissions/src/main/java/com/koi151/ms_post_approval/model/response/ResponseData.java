package com.koi151.ms_post_approval.model.response;

import com.koi151.ms_post_approval.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ResponseData extends AbstractDTO<Object> {

    @Serial
    private static final long serialVersionUID = -7081110393196421994L;

    private Object data;
    private String description;
    private int status = 200;
    private boolean isSuccess = true;
}
