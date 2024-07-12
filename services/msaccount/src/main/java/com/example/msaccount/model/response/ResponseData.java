package com.example.msaccount.model.response;

import com.example.msaccount.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ResponseData extends AbstractDTO<Object> {

    @Serial
    private static final long serialVersionUID = -7145586484567833095L;

    private short status = 200;
    private Object data;
    private String desc;
    private boolean isSuccess = true;
}
