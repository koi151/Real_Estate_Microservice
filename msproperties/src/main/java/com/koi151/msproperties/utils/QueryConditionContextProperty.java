package com.koi151.msproperties.utils;

import com.koi151.msproperties.entity.PropertyEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QueryConditionContextProperty {
    // final prevent changing reference
    private final CriteriaBuilder criteriaBuilder; // used to build predicates and other parts of queries
    private final CriteriaQuery<?> criteriaQuery;
    private final Root<PropertyEntity> root; // provide access to entity method, relationship,..
    private final List<Predicate> predicates; // condition in Criteria query
}
