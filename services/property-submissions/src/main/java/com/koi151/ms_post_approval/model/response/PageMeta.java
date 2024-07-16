package com.koi151.ms_post_approval.model.response;

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
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
}