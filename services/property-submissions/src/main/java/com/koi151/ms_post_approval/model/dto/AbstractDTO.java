package com.koi151.ms_post_approval.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2441707196538770288L;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long totalItems;
//    private Integer limit;
    private Integer maxPageItems;
    private Integer currentPage;
    private Integer totalPages;
    private String searchValue;
    private String createdBy;
    private String modifiedBy;
}
