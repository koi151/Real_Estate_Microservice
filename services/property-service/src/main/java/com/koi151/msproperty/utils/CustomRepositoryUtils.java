package com.koi151.msproperty.utils;


import com.koi151.msproperty.enums.QueryFieldOptionEnum;
import com.koi151.msproperty.repository.custom.impl.PropertyRepositoryImpl;
import com.koi151.msproperty.utils.QueryContext.QueryContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

    @PersistenceContext // used to inject an EntityManager into a class
    private static EntityManager entityManager;

//     Apply pagination to the TypedQuery
    public static <T> Page<T> applyPagination(TypedQuery<T> query, Pageable pageable) {
        int totalElements = query.getResultList().size(); // Total elements before pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<T> results = query.getResultList();
        logger.info("Number of properties fetched: {}", results.size());
        return new PageImpl<>(results, pageable, totalElements);
    }


    // Apply pagination to the TypedQuery
//    public static <T> Page<T> applyPagination(EntityManager entityManager, TypedQuery<T> query, Pageable pageable) {
//        // Set pagination parameters directly on the TypedQuery
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        // Fetch only the requested page of results
//        List<T> results = query.getResultList();
//        logger.info("Number of properties fetched: {}", results.size());
//
//        // Get the total count using a separate count query
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Root<T> countRoot = countQuery.from(query.getResultType());
//
//        countQuery.select(cb.count(countRoot));
//
//        // Copy predicates from the original query to the count query
//        Predicate restrictions = query.unwrap(JpaQueryableCriteria.class).getRestriction();
//        if (restrictions != null) {
//            countQuery.where(restrictions);
//        }
//
//        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
//
//        return new PageImpl<>(results, pageable, totalCount);
//    }








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