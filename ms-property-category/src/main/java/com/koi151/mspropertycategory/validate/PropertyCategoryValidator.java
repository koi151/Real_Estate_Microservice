package com.koi151.mspropertycategory.validate;

import com.koi151.mspropertycategory.entity.payload.request.PropertyCategoryRequest;
import customExceptions.FieldRequiredException;

public class PropertyCategoryValidator {
    public static void validateCategoryRequest(PropertyCategoryRequest categoryRequest) {
        if (categoryRequest.getTitle() == null) {
            throw new FieldRequiredException("Title of category is null");
        }
    }
}
