package com.koi151.notification.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageMeta {
    private Long totalElements;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
}