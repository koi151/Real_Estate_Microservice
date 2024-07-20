package com.koi151.msproperties.utils.QueryContext;

import com.koi151.msproperties.entity.PropertyCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public record QueryConditionContextPropertyCategory(// record is immutable, replace lombok annotation - available since Java 14
                                                    CriteriaBuilder criteriaBuilder,
                                                    CriteriaQuery<?> criteriaQuery,
                                                    Root<PropertyCategory> root,
                                                    List<Predicate> predicates
) {}
