package com.koi151.ms_post_approval.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public record QueryContext<T>(
    CriteriaBuilder criteriaBuilder,
    CriteriaQuery<?> criteriaQuery,
    Root<T> root,
    List<Predicate> predicates
) {}
