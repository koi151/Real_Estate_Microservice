package com.koi151.notification.model.response;


import com.koi151.notification.model.dto.AbstractDTO;
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
    private static final long serialVersionUID = 8352406533346155572L;

    private Object data;
    private String description;
    private int status = 200;
    private boolean isSuccess = true;
    private PageMeta meta;
}


//@Getter
//@Setter
//@SuperBuilder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ResponseData extends AbstractDTO<Object> {
//
//    @Serial
//    private static final long serialVersionUID = -7081110393196421994L;
//
//    private Object data;
//    private String description;
//    private int status = 200;
//    private boolean isSuccess = true;
//    private PageMeta meta;
//}
