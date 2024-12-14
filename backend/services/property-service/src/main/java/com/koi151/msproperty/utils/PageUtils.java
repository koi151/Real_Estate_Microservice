package com.koi151.msproperty.utils;

import com.koi151.msproperty.model.request.PaginationRequest;
import org.springframework.data.domain.PageRequest;

public class PageUtils {

    public static PageRequest buildPageRequest(PaginationRequest pagination, int maxPageSize) {
        return PageRequest.of(
            pagination.getPage() - 1,
            Math.min(pagination.getSize(), maxPageSize),
            SortingUtils.buildSort(pagination.getSort())
        );
    }
}
