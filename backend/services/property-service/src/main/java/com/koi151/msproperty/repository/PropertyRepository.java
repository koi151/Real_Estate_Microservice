package com.koi151.msproperty.repository;

import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.repository.custom.PropertyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>
                                        , PropertyRepositoryCustom
                                        , JpaSpecificationExecutor<Property> {

//    @Query("SELECT p.propertyPostService FROM property p WHERE p.propertyId = :id")
//    PropertyPostServiceEntity findPropertyPostServiceByPropertyId(@Param("id") Long id);
    boolean existsByPropertyIdAndDeletedAndStatus(Long propertyId, boolean deleted, StatusEnum status);
    Page<Property> findByDeleted(boolean deleted, Pageable pageable);
    Page<Property> findByCategoryIdAndDeleted(Integer categoryId, boolean deleted, Pageable pageable);
    Page<Property> findByAccountIdAndDeleted(String accountId, boolean deleted, Pageable pageable);
    Page<Property> findByStatus(StatusEnum status, Pageable pageRequest);
    Optional<Property> findByPropertyIdAndDeleted(Long propertyId, boolean deleted);
}
