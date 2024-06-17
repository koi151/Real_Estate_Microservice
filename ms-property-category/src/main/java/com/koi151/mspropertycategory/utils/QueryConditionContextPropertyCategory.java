package com.koi151.mspropertycategory.utils;

import com.koi151.mspropertycategory.entity.PropertyCategoryEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public record QueryConditionContextPropertyCategory( // record is immutable, replace lombok annotation - available since Java 14
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<PropertyCategoryEntity> criteriaQuery,
        Root<PropertyCategoryEntity> root, List<Predicate> predicates) {
}
