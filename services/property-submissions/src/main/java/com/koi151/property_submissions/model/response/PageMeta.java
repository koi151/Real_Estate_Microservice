package com.koi151.property_submissions.model.response;

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