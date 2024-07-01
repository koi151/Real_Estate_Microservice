package com.koi151.mspropertycategory.validate;

import com.koi151.mspropertycategory.model.request.PropertyCategoryCreateRequest;
import customExceptions.FieldRequiredException;

public class PropertyCategoryValidator {
    public static void validateCategoryRequest(PropertyCategoryCreateRequest categoryRequest) {
        if (categoryRequest.getTitle() == null) {
            throw new FieldRequiredException("Title of category is null");
        }
    }
}
