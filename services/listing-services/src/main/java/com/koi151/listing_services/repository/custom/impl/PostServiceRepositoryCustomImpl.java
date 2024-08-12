package com.koi151.listing_services.repository.custom.impl;

import com.koi151.listing_services.repository.custom.PostServiceRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PostServiceRepositoryCustomImpl implements PostServiceRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Set<Long> findMissingPostServiceIds(Set<Long> ids) {
        // Create a series of SELECT
        String idSelects = ids.stream()
                .map(id -> "SELECT " + id + " AS id")
                .collect(Collectors.joining(" UNION ALL "));

        String sql = "SELECT v.id FROM ( " + idSelects + ") AS v " +
                "LEFT JOIN post_service ps ON v.id = ps.post_service_id " +
                "WHERE ps.post_service_id IS NULL";

        // specifying the result type as Long
        List<Long> resultList = entityManager.createNativeQuery(sql, Long.class).getResultList();

        return new HashSet<>(resultList);
    }
}
