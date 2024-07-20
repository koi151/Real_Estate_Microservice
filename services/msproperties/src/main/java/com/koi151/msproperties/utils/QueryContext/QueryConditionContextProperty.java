package com.koi151.msproperties.utils.QueryContext;

import com.koi151.msproperties.entity.Property;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Map;

public record QueryConditionContextProperty(
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<Property> criteriaQuery,
        Root<Property> root,
        List<Predicate> predicates,
        Map<String, Join<Property, ?>> joins
) {
    public void addJoin(String name, Join<Property, ?> join) {
        joins.put(name, join);
    }
}
