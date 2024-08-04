package com.koi151.msproperties.utils.QueryContext;

import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Map;

public record QueryContext<T>(
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<?> criteriaQuery,
        Root<T> root,
        List<Predicate> predicates,
        Map<String, Join<T, ?>> joins

) {
    public void addJoin(String name, Join<T, T> join) {
        joins.put(name, join);
    }

    public Map<String, Join<T, ?>> getJoins() {
        return joins;
    }
}