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

/**
 * @param criteriaBuilder final prevent changing reference used to build predicates and other parts of queries
 * @param root            provide access to entity method, relationship,..
 * @param predicates      condition in Criteria query
 */

public record QueryConditionContextProperty(
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<?> criteriaQuery,

        Root<PropertyEntity> root,
        List<Predicate> predicates
) {}
