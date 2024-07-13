package com.koi151.msproperties.utils;

import com.koi151.msproperties.model.request.PropertySearchRequest;
import com.koi151.msproperties.utils.QueryContext.QueryConditionContextProperty;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRepositoryUtils {

    // Apply pagination to the TypedQuery
    public static <T> Page<T> applyPagination(TypedQuery<T> query, Pageable pageable) {
        int totalElements = query.getResultList().size(); // Total elements before pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> results = query.getResultList();
        return new PageImpl<>(results, pageable, totalElements);
    }

    private static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
        return excludedFields.contains(fieldName) ||
                excludedFields.stream().anyMatch(fieldName::startsWith);
    }

    // Apply query within table
    public static <T, E> void appendNormalQueryConditions( // T: entity type - E: request type
            E request,
            Root<T> root,
            CriteriaBuilder criteriaBuilder,
            List<Predicate> predicates,
            Set<String> excludedFields
    ) {
        Field[] fields = request.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(request);

                if (!isExcludedField(fieldName, excludedFields) && value != null && !value.toString().isEmpty()) {
                    Path<?> path = root.get(fieldName);
                    if (value instanceof Integer || value instanceof Float || value instanceof Long) {
                        predicates.add(criteriaBuilder.equal(path, value));
                    } else if (value instanceof String) {
                        predicates.add(criteriaBuilder.like(path.as(String.class), "%" + value + "%"));
                    } else if (value.getClass().isEnum()) {
                        predicates.add(criteriaBuilder.equal(path, ((Enum<?>) value).name()));
                    }
                }
            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value: " + ex.getMessage());
            }
        }
    }
}
