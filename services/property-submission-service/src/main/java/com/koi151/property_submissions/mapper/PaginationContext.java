package com.koi151.property_submissions.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationContext {
    private final int pageNumber;
    private final int pageSize;
}
