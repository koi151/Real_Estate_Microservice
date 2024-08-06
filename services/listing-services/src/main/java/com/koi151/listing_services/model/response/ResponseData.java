package com.koi151.listing_services.model.response;

import com.koi151.listing_services.model.dto.AbstractDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ResponseData extends AbstractDTO<Object> {

    @Serial
    private static final long serialVersionUID = 2616977589417604063L;

    private Object data;
    private String desc;
    private int status = 200;
    private boolean isSuccess = true;
}
