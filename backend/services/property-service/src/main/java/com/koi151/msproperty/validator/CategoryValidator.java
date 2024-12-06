package com.koi151.msproperty.validator;

import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.repository.PropertyCategoryRepository;
import com.koi151.property_submissions.customExceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final PropertyCategoryRepository propertyCategoryRepository;

    public PropertyCategory isValidParentCategory(Long id) {
        return (id == null) ? null
            : propertyCategoryRepository.findByCategoryIdAndDeleted(id, false)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not existed with id: " + id));
    }
}
