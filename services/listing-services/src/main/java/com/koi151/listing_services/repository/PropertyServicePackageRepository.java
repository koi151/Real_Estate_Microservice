package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.repository.custom.PropertyServicePackageRepositoryCustom;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface PropertyServicePackageRepository extends JpaRepository<PropertyServicePackage, Long>, PropertyServicePackageRepositoryCustom {

    // Since JPQL does not support complex grouping with multiple DTOs directly in a single query, used Criteria instead
//    @Query("SELECT " +
//                "new com.koi151.listing_services.model.dto.PropertyPostPackageSummaryDTO(prk.packageType, prk.propertyServicePackageId, " +
//                "new com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO(ps.postServiceId, ps.name, ps.availableUnits)) " +
//            "FROM property_service_package prk " +
//            "LEFT JOIN prk.postServicePackages pok " +
//            "LEFT JOIN pok.postService ps " +
//            "WHERE prk.propertyServicePackageId = :id " +
//            "GROUP BY prk.packageType, prk.propertyServicePackageId")
//    PropertyPostPackageSummaryDTO findByPropertyServicePackageIdWithPostServices(@Param("id") Long id);
}