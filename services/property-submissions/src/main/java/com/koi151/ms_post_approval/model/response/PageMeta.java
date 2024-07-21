package com.koi151.ms_post_approval.model.response;

import lombok.*;

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