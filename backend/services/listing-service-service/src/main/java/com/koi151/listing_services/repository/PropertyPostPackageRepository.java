package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyPostPackageRepository extends JpaRepository<PropertyServicePackage, Long> {
    boolean existsByPropertyIdAndStatus(Long id, Status status);
}
