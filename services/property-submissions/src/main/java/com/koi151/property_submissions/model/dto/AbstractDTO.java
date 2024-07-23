package com.koi151.property_submissions.model.dto;

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
    private static final long serialVersionUID = -2441707196538770288L;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String searchValue;
    private String createdBy;
    private String modifiedBy;
}
