package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.PostServicePricing;
import com.koi151.listing_services.enums.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostServicePricingRepository extends JpaRepository<PostServicePricing, Long> {

    @Query("SELECT psp.price FROM post_service_pricing psp " +
           "WHERE psp.postServicePricingId IN :ids")
    List<BigDecimal> findPricingListByIds(@Param("ids") List<Long> ids);

    @Query("SELECT psp.price FROM post_service_pricing psp " +
           "WHERE psp.postService.postServiceId = :postServiceId " +
           "AND psp.packageType = :packageType")
    Optional<BigDecimal> findPriceByPostServiceIdAndPackageType(@Param("postServiceId") Long postServiceId, @Param("packageType") PackageType packageType);
}
