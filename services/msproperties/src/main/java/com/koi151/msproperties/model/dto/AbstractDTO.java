package com.koi151.msproperties.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -699889113463942885L;

    private LocalDateTime createdDate;
    //    private String createdBy;
    private LocalDateTime modifiedDate;
    //    private String modifiedBy;
    private Integer maxPageItems;
    private Integer currentPage = 1;
    private String tableId;
    private Integer limit;
    private Integer totalPages;
    private Long totalItems;
    private String searchValue;
}