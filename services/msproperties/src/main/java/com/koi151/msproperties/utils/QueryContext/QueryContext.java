package com.koi151.msproperties.utils.QueryContext;

import com.koi151.msproperties.entity.Property;
import jakarta.persistence.criteria.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public record QueryContext<T>(
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<?> criteriaQuery,
        Root<T> root,
        List<Predicate> predicates,
        Map<String, Join<T, ?>> joins

) {
    public void addJoin(String name, Join<T, ?> join) {
        joins.put(name, join);
    }
}