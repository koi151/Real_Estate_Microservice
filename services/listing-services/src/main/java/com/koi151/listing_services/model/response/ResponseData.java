package com.koi151.listing_services.model.response;

import com.koi151.listing_services.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ResponseData extends AbstractDTO<Object> {

    @Serial
    private static final long serialVersionUID = 2616977589417604063L;

    private Object data;
    private String desc;
    private int status = 200;
    private boolean isSuccess = true;
}
