package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PropertyServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyServicePackageRepository extends JpaRepository<PropertyServicePackage, Long> {
}
