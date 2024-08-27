package com.koi151.payment.model.response;

import com.koi151.payment.model.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData extends AbstractDTO<Object> {
    @Serial
    private static final long serialVersionUID = -1284028147318128526L;

    private Object data;
    private String description;
    private int status = 200;
    private boolean isSuccess = true;
    private PageMeta meta;
}
