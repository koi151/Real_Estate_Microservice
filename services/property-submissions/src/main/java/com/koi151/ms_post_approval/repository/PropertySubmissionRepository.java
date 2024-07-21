package com.koi151.ms_post_approval.repository;


import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.repository.custom.CustomPropertySubmissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertySubmissionRepository extends JpaRepository<PropertySubmission, Long>, CustomPropertySubmissionRepository {

    boolean existsByPropertyIdAndDeleted(Long propertyId, boolean deleted);
    boolean existsByReferenceCode(String code);
    boolean existsByAccountIdAndDeleted(Long accountId, boolean deleted);
    Page<PropertySubmission> findPropertySubmissionByAccountIdAndDeleted(Long id, boolean deleted, Pageable pageable);
    Page<PropertySubmission> findAllByDeleted(Pageable pageable, boolean deleted); // remove del, add in search criteria
}
