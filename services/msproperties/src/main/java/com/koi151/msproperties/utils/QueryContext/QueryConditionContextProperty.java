package com.koi151.msproperties.utils.QueryContext;

import com.koi151.msproperties.entity.PropertyEntity;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Map;

public record QueryConditionContextProperty(
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<PropertyEntity> criteriaQuery,
        Root<PropertyEntity> root,
        List<Predicate> predicates,
        Map<String, Join<PropertyEntity, ?>> joins
) {
    public void addJoin(String name, Join<PropertyEntity, ?> join) {
        joins.put(name, join);
    }
}
