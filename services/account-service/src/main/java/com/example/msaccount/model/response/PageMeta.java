package com.example.msaccount.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageMeta {
    private Long totalElements;
    private int pageSize;
    private int pageNumber;
    private int totalPages;
}