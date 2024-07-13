package com.koi151.msproperties.repository.custom.impl;


import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyCategoryRepositoryCustom;
import com.koi151.msproperties.utils.CustomRepositoryUtils;
import com.koi151.msproperties.utils.QueryContext.QueryConditionContextPropertyCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

    @Override
    public Page<PropertyCategoryEntity> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertyCategoryEntity> cq = cb.createQuery(PropertyCategoryEntity.class);
        Root<PropertyCategoryEntity> root = cq.from(PropertyCategoryEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (request != null) {
            QueryConditionContextPropertyCategory context = new QueryConditionContextPropertyCategory(cb, cq, root, predicates);

            // Apply normal query condition
            Set<String> excludedFields = new HashSet<>(); // currently empty
            CustomRepositoryUtils.appendNormalQueryConditions(request, root, cb, predicates, excludedFields);
        }

        // Apply predicates if there are any, otherwise no filter (select all)
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<PropertyCategoryEntity> typedQuery = entityManager.createQuery(cq);
        return CustomRepositoryUtils.applyPagination(typedQuery, pageable);
    }
}
