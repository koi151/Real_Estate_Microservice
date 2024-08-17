package com.koi151.listing_services.repository.custom.impl;

import com.koi151.listing_services.entity.PostService;
import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.enums.PackageType;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.repository.custom.PropertyServicePackageRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Primary
public class PropertyServicePackageRepositoryCustomImpl implements PropertyServicePackageRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public PropertyServicePackageSummaryDTO findPropertyServicePackageWithsPostServices(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<PropertyServicePackage> root = cq.from(PropertyServicePackage.class);

        Join<PropertyServicePackage, PostServicePackage> propertyServicePackageJoin = root.join("postServicePackages", JoinType.LEFT);
        Join<PostServicePackage, PostService> postServiceJoin = propertyServicePackageJoin.join("postService", JoinType.LEFT);

        // Convert the PackageType enum to its string representation
        cq.multiselect(
            root.get("propertyServicePackageId"),
            cb.selectCase(root.get("packageType"))
                .when(PackageType.STANDARD, PackageType.STANDARD.getPackageName())
                .when(PackageType.PREMIUM, PackageType.PREMIUM.getPackageName())
                .when(PackageType.EXCLUSIVE, PackageType.EXCLUSIVE.getPackageName()),
            postServiceJoin.get("postServiceId"),
            postServiceJoin.get("name"),
            postServiceJoin.get("availableUnits")
        ).where(cb.equal(root.get("propertyServicePackageId"), id));

        List<Object[]> results = entityManager.createQuery(cq).getResultList();

        if (results.isEmpty()) {
            return null;
        }

        // Process the results into a DTO
        PropertyServicePackageSummaryDTO propertyServicePackageSummaryDTO = null;
        List<PostServiceBasicInfoDTO> postServiceBasicInfoDTOs = new ArrayList<>();

        for (Object[] result : results) {
            Long propertyServicePackageId = (Long) result[0];
            String packageType = (String) result[1];
            Long postServiceId = (Long) result[2];
            String postServiceName = (String) result[3];
            Integer availableUnits = (Integer) result[4];

            if (propertyServicePackageSummaryDTO == null) {
                propertyServicePackageSummaryDTO = PropertyServicePackageSummaryDTO.builder()
                    .propertyPostPackageId(propertyServicePackageId)
                    .packageType(packageType)
                    .postServiceBasicInfoDTOs(postServiceBasicInfoDTOs)
                    .build();
            }

            postServiceBasicInfoDTOs.add(new PostServiceBasicInfoDTO(postServiceId, postServiceName, availableUnits));
        }

        return propertyServicePackageSummaryDTO;
    }
}
