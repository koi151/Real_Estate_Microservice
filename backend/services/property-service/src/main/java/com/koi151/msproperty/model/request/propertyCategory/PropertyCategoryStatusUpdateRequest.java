package com.koi151.msproperty.model.request.propertyCategory;

import com.koi151.msproperty.enums.CategoryStatusEnum;

public record PropertyCategoryStatusUpdateRequest (
    CategoryStatusEnum status
) {}
