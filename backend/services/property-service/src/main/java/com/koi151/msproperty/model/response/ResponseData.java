package com.koi151.msproperty.model.response;

import com.koi151.msproperty.model.dto.AbstractDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResponseData extends AbstractDTO<Object> {
    @Serial
    private static final long serialVersionUID = 5760721239451228077L;

    private Object data;
    private String desc;
//    private int status = 200;
//    private boolean isSuccess = true;
}
