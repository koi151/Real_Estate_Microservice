package com.koi151.msproperty.repository.custom.impl;


import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategorySearchRequest;
import com.koi151.msproperty.repository.custom.PropertyCategoryRepositoryCustom;
import com.koi151.msproperty.utils.CustomRepositoryUtils;
import com.koi151.msproperty.utils.QueryContext.QueryConditionContextPropertyCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Page<PropertyCategory> getPropertyCategoryByCriteria(PropertyCategorySearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertyCategory> cq = cb.createQuery(PropertyCategory.class);
        Root<PropertyCategory> root = cq.from(PropertyCategory.class);
        List<Predicate> predicates = new ArrayList<>();

        if (request != null) {
            QueryConditionContextPropertyCategory context = new QueryConditionContextPropertyCategory(cb, cq, root, predicates);

            // Apply normal query condition
//            Set<String> excludedFields = new HashSet<>(); // currently empty
        }

        // Apply predicates if there are any, otherwise no filter (select all)
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<PropertyCategory> typedQuery = entityManager.createQuery(cq);
        return CustomRepositoryUtils.applyPagination(typedQuery, pageable);
    }
}

