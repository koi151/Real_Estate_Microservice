package com.koi151.listing_services.repository;

import com.koi151.listing_services.entity.Promotion;
import com.koi151.listing_services.enums.PackageType;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("SELECT p.discountPercentage, p.priceDiscount FROM promotion p " +
           "WHERE p.promotionId IN :ids")
    List<Object[]> findPromotionsByPostServiceIds(@Param("ids") List<Long> ids);

    @Query("SELECT " +
                "COALESCE(p.discountPercentage, 0), " +
                "COALESCE(p.priceDiscount, 0) " +
            "FROM promotion p " +
            "WHERE p.postService.postServiceId = :postServiceId AND p.packageType = :packageType")
    Optional<Tuple> findPromotionInfoByPostServiceIdAndPackageType(@Param("postServiceId") Long postServiceId, @Param("packageType") PackageType packageType);
}
