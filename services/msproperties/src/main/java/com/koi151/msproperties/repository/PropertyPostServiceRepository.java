package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.PropertyPostService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyPostServiceRepository extends JpaRepository<PropertyPostService, Long> {
    PropertyPostService findByPropertyPropertyIdAndPropertyDeleted(Long propertyId, boolean deleted);
}
