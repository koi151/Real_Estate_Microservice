package com.koi151.payment.model.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class AbstractDTO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -8379602400130296352L;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}
