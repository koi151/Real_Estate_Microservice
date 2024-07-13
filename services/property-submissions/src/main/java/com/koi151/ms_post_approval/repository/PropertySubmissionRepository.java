package com.koi151.ms_post_approval.repository;


import com.koi151.ms_post_approval.entity.PropertySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertySubmissionRepository extends JpaRepository<PropertySubmission, Long> {

    boolean existsByPropertyIdAndDeleted(Long propertyId, boolean deleted);
}
