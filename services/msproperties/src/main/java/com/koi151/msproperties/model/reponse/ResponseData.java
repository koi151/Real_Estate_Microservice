package com.koi151.msproperties.model.reponse;

import com.koi151.msproperties.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class ResponseData extends AbstractDTO<Object> {
    @Serial
    private static final long serialVersionUID = 5760721239451228077L;

    private Object data;
    private String desc;
    private int status = 200;
    private boolean isSuccess = true;


}
