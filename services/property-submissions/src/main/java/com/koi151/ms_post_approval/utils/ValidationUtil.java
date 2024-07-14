package com.koi151.ms_post_approval.utils;

import com.koi151.ms_post_approval.entity.PropertySubmission;
import org.springframework.data.domain.Page;

import java.util.Collection;

public class ValidationUtil {
    public static <T extends Collection<?>> boolean isEmptyOrNull(T collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T extends Collection<?>> boolean isEmptyOrNullPage(Page<PropertySubmission> collection) {
        return collection == null || collection.isEmpty();
    }

}
