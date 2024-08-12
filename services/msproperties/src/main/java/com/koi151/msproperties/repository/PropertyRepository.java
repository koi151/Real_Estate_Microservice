package com.koi151.msproperties.repository;

import com.koi151.msproperties.entity.Property;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>, PropertyRepositoryCustom {

//    @Query("SELECT p.propertyPostService FROM property p WHERE p.propertyId = :id") // created new repository instead to adhere SRP
//    PropertyPostServiceEntity findPropertyPostServiceByPropertyId(@Param("id") Long id);

    boolean existsByPropertyIdAndDeletedAndStatus(Long propertyId, boolean deleted, StatusEnum status);
    Page<Property> findByDeleted(boolean deleted, Pageable pageable);
    Page<Property> findByCategoryIdAndDeleted(Integer categoryId, boolean deleted, Pageable pageable);
    Page<Property> findByAccountIdAndDeleted(Long accountId, boolean deleted, Pageable pageable);
    Page<Property> findByStatus(StatusEnum status, Pageable pageRequest);
    Optional<Property> findByPropertyIdAndDeleted(Long propertyId, boolean deleted);
}
