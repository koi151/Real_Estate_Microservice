package com.koi151.listing_services.repository.custom.impl;

import com.koi151.listing_services.entity.*;
import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.repository.custom.PropertyServicePackageRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
public class PropertyServicePackageRepositoryCustomImpl implements PropertyServicePackageRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public PropertyServicePackageSummaryDTO findPropertyServicePackageByCriteria(Map<String, String> params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);

        Root<PropertyServicePackage> root = cq.from(PropertyServicePackage.class);
        Join<PropertyServicePackage, PostServicePackage> propertyServicePackageJoin = root.join("postServicePackages", JoinType.LEFT);
        Join<PostServicePackage, PostService> postServiceJoin = propertyServicePackageJoin.join("postService", JoinType.LEFT);
        Join<PostService, PostServicePricing> postServicePricingJoin = postServiceJoin.join("postServicePricings", JoinType.LEFT);
        Join<PostService, Promotion> promotionJoin = postServiceJoin.join("promotions", JoinType.LEFT);

        // Build the select clause with multiselect
        cq.multiselect(
            root.get("propertyServicePackageId").alias("propertyServicePackageId"),
            cb.selectCase(root.get("packageType"))
                .when(PackageType.STANDARD, PackageType.STANDARD.getPackageName())
                .when(PackageType.PREMIUM, PackageType.PREMIUM.getPackageName())
                .when(PackageType.EXCLUSIVE, PackageType.EXCLUSIVE.getPackageName())
                .alias("packageType"),
            postServiceJoin.get("postServiceId").alias("postServiceId"),
            postServiceJoin.get("name").alias("name"),
            postServiceJoin.get("availableUnits").alias("availableUnits"),
            postServicePricingJoin.get("price").alias("price"),
            promotionJoin.get("discountPercentage").alias("discountPercentage"),
            promotionJoin.get("priceDiscount").alias("priceDiscount")
        );
//        .where(
//            cb.equal(root.get("packageId"), request.packageId()),
//            cb.equal(postServicePricingJoin.get("packageType"), root.get("packageType")) // Ensure package type matches for pricing
//        )

//         Prepare predicates for where clause
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(postServicePricingJoin.get("packageType"), root.get("packageType"))); // Ensure package type matches for pricing

        if (params.get("packageId") != null)
            predicates.add(cb.equal(root.get("propertyServicePackageId"), params.get("packageId")));
        if (params.get("propertyId") != null)
            predicates.add(cb.equal(root.get("propertyId"), params.get("propertyId")));

        // Apply where clause with the list of predicates
        cq.where(predicates.toArray(new Predicate[0]));

        List<Tuple> results = entityManager.createQuery(cq).getResultList();

        if (results.isEmpty()) {
            return null;  // Return null if no results found
        }

        List<PostServiceBasicInfoDTO> postServiceBasicInfoDTOs = new ArrayList<>();
        BigDecimal totalFee = BigDecimal.ZERO;

        for (Tuple result : results) {
            Long postServiceId = result.get("postServiceId", Long.class);
            String postServiceName = result.get("name", String.class);
            BigDecimal servicePrice = result.get("price", BigDecimal.class);
            Integer availableUnits = result.get("availableUnits", Integer.class);

            BigDecimal standardPrice = result.get("price", BigDecimal.class);
            BigDecimal discountPercentage = result.get("discountPercentage", BigDecimal.class);
            BigDecimal priceDiscount = result.get("priceDiscount", BigDecimal.class);

            // Calculate total fee considering discounts
            if (standardPrice != null) {
                BigDecimal priceDiscountedFromPromo = (discountPercentage != null) ? standardPrice.multiply(discountPercentage.divide(new BigDecimal(100), RoundingMode.HALF_EVEN)) : BigDecimal.ZERO;
                BigDecimal discountPrice = (priceDiscount != null) ? priceDiscount : BigDecimal.ZERO;
                totalFee = totalFee.add(standardPrice.subtract(priceDiscountedFromPromo).subtract(discountPrice).max(BigDecimal.ZERO));
            }

            // Add the post service basic info DTO to the list postServiceId, postServiceName, availableUnits
            postServiceBasicInfoDTOs.add(PostServiceBasicInfoDTO.builder()
                .postServiceId(postServiceId)
                .name(postServiceName)
                .availableUnits(availableUnits)
                .price(servicePrice)
                .build());
        }

        // Get the first result to extract package details
        Tuple firstResult = results.get(0);

        return PropertyServicePackageSummaryDTO.builder()
            .propertyPostPackageId(firstResult.get("propertyServicePackageId", Long.class))
            .packageType(firstResult.get("packageType", String.class))
            .totalFee(totalFee)
            .postServiceBasicInfo(postServiceBasicInfoDTOs)
            .build();
    }




}
