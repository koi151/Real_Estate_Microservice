package com.koi151.ms_post_approval.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationContext {
    private final int pageNumber;
    private final int pageSize;
}
