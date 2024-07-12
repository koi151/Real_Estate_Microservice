package com.example.msaccount.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private Integer totalItems;
    private Integer totalSubItems;
    private String searchValue;

    public Integer getTotalPages() {
        if (maxPageItems == null) return null;
        if (totalItems == 0 || maxPageItems == 0)
            return 0;
        return (totalItems + maxPageItems - 1) / maxPageItems;
    }

}