package com.koi151.notification.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -9009869114005277761L;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String searchValue;
    private String createdBy;
    private String modifiedBy;
}
