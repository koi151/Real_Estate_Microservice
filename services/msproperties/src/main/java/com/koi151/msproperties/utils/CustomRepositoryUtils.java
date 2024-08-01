package com.koi151.msproperties.utils;


import com.koi151.msproperties.enums.QueryFieldOptionEnum;
import com.koi151.msproperties.repository.custom.impl.PropertyRepositoryImpl;
import com.koi151.msproperties.utils.QueryContext.QueryContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CustomRepositoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyRepositoryImpl.class);


    // Apply pagination to the TypedQuery
    public static <T> Page<T> applyPagination(TypedQuery<T> query, Pageable pageable) {
        int totalElements = query.getResultList().size(); // Total elements before pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> results = query.getResultList();
        logger.info("Number of properties fetched: {}", results.size());
        return new PageImpl<>(results, pageable, totalElements);
    }

    public static <T, E> void appendNormalQueryConditions(E request, QueryContext<T> context, Map<String, QueryFieldOptionEnum> queryOptions) {

        for (Field field : request.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(request);

                QueryFieldOptionEnum option = queryOptions.get(fieldName);

                if (option != null && value != null && !value.toString().isEmpty()) { // null check for option
                    Path<?> path = context.root().get(fieldName);

                    switch (option) {
                        case LIKE_STRING ->
                                context.predicates().add(context.criteriaBuilder().like(path.as(String.class), "%" + value + "%"));
                        case EXACT_STRING ->
                                context.predicates().add(context.criteriaBuilder().equal(path.as(String.class), value));
                        case NUMBER -> {
                            if (value instanceof Number) {
                                context.predicates().add(context.criteriaBuilder().equal(path, value));
                            }
                        }
                        case ENUM -> {
                            if (value.getClass().isEnum()) {
                                context.predicates().add(context.criteriaBuilder().equal(path, value)); // Pass Enum
                            }
                        }
                        default -> System.err.println("Unsupported QueryFieldOption: " + option);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error accessing field '" + field.getName() + "': " + ex.getMessage());
                // Consider rethrowing a custom exception for better error handling
            }
        }
    }
}


// areaFrom, priceFrom, To