package com.koi151.mspropertycategory.repository.custom.impl;


import com.koi151.mspropertycategory.entity.PropertyCategoryEntity;
import com.koi151.mspropertycategory.model.request.PropertyCategorySearchRequest;
import com.koi151.mspropertycategory.repository.custom.PropertyCategoryRepositoryCustom;
import com.koi151.mspropertycategory.utils.QueryConditionContextPropertyCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyCategoryRepositoryImpl implements PropertyCategoryRepositoryCustom {

    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
        return excludedFields.contains(fieldName) ||
                excludedFields.stream().anyMatch(fieldName::startsWith);
    }

    private static void appendNormalQueryCondition (PropertyCategorySearchRequest request, QueryConditionContextPropertyCategory context) {
        Set<String> excludedFields = new HashSet<>(Set.of("sample"));

        Field[] fields = PropertyCategorySearchRequest.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true); // allow access to private fields
                String fieldName = field.getName();
                Object value = field.get(request); // If this Field object is enforcing Java language access control, and the underlying field is inaccessible, the method throws an IllegalAccessException.

                if (!isExcludedField(fieldName, excludedFields) && value != null && !value.toString().isEmpty()) {
                    Path<?> path = context.root().get(fieldName);
                    if (value instanceof String) {
                        context.predicates().add(context.criteriaBuilder().like(path.as(String.class), "%" + value + "%")); // the "like" method require String casting
                    } else if (value instanceof Integer || value instanceof Float || value instanceof Long) {
                        context.predicates().add(context.criteriaBuilder().equal(path, value));
                    } else if (value.getClass().isEnum()) {
                        context.predicates().add(context.criteriaBuilder().equal(path, ((Enum<?>) value).name()));
                    }
                }

            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value: " + ex.getMessage());
            }
        }
    }

//    private static void appendSpecialQueryConditions(PropertyCategorySearchRequest request, QueryConditionContextPropertyCategory context) {
//        Root<PropertyCategoryEntity> root = context.root();
//        List<Predicate> predicates = context.predicates();
//        CriteriaBuilder cb = context.criteriaBuilder();
//    }

    @Override
    public List<PropertyCategoryEntity> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertyCategoryEntity> cq = cb.createQuery(PropertyCategoryEntity.class);
        Root<PropertyCategoryEntity> root = cq.from(PropertyCategoryEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (request != null) {
            QueryConditionContextPropertyCategory context = new QueryConditionContextPropertyCategory(cb, cq, root, predicates);
            appendNormalQueryCondition(request, context);
            // appendSpecialQueryConditions(request, context);
        }

        // Apply predicates if there are any, otherwise no filter (select all)
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<PropertyCategoryEntity> typedQuery = entityManager.createQuery(cq);
        return typedQuery.getResultList();
    }

}
