package com.koi151.property_submissions.repository.custom.impl;

import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.enums.QueryFieldOption;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import com.koi151.property_submissions.repository.custom.CustomPropertySubmissionRepository;
import com.koi151.property_submissions.utils.CustomRepositoryUtils;
import com.koi151.property_submissions.utils.QueryContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class CustomPropertySubmissionRepositoryImpl implements CustomPropertySubmissionRepository {

    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    @Override
    public Page<PropertySubmission> findPropertySubmissionsByCriteria(PropertySubmissionSearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertySubmission> cq = cb.createQuery(PropertySubmission.class);
        Root<PropertySubmission> root = cq.from(PropertySubmission.class);
        List<Predicate> predicates = new ArrayList<>();

        if (request != null) {
            QueryContext<PropertySubmission> context = new QueryContext<>(cb, cq, root, predicates);

            // Apply default query options (LIKE for Strings, EXACT for others)
            Map<String, QueryFieldOption> queryOptions = new HashMap<>();
            for (Field field : request.getClass().getDeclaredFields()) { // can seperated into other method
                var type = field.getType();
                if (type == String.class) {
                    queryOptions.put(field.getName(), QueryFieldOption.LIKE_STRING);
                } else if (Number.class.isAssignableFrom(type)) {
                    queryOptions.put(field.getName(), QueryFieldOption.NUMBER);
                } else if (field.getType().isEnum()) {
                    queryOptions.put(field.getName(),  QueryFieldOption.ENUM);
                }
            }

            // Override for specific fields
            queryOptions.put("referenceCode", QueryFieldOption.EXACT_STRING);
//            Exclude specific fields
//            queryOptions.put("...", QueryFieldOption.EXCLUDE);

            CustomRepositoryUtils.appendNormalQueryConditions(request, context, queryOptions);
        }

        // Apply predicates if there are any, otherwise no filter (select all)
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<PropertySubmission> typedQuery = entityManager.createQuery(cq);
        return CustomRepositoryUtils.applyPagination(typedQuery, pageable);
    }


}
