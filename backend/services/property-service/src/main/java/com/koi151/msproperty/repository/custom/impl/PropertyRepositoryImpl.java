package com.koi151.msproperty.repository.custom.impl;

import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.model.projection.PropertyForRentProjection;
import com.koi151.msproperty.model.projection.PropertyForSaleProjection;
import com.koi151.msproperty.model.projection.PropertySearchProjection;
import com.koi151.msproperty.model.projection.RoomSearchProjection;
import com.koi151.msproperty.model.request.property.PropertyFilterRequest;
import com.koi151.msproperty.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperty.specification.PropertySpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
@RequiredArgsConstructor
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<PropertySearchProjection> findPropertiesForAdmin(PropertyFilterRequest request, Pageable pageable) {
        return searchProperties(request, pageable, false);
    }

    @Override
    public Page<PropertySearchProjection> findPropertiesForClient(PropertyFilterRequest request, Pageable pageable) {
        return searchProperties(request, pageable, true);
    }

    private Page<PropertySearchProjection> searchProperties(PropertyFilterRequest request, Pageable pageable, boolean isClientView) {
        Specification<Property> spec = PropertySpecifications.buildSpecification(request, isClientView);

        // build criteriaQuery
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> cq = cb.createQuery(Property.class);
        Root<Property> root = cq.from(Property.class);
        cq.select(root).where(spec.toPredicate(root, cq, cb));

        long total = getTotalCount(spec);

        // sort
        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                .map(order -> order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty())))
                .collect(Collectors.toList());
            cq.orderBy(orders);
        }

        // query with pagination
        TypedQuery<Property> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Property> properties = query.getResultList();
        List<PropertySearchProjection> projections = properties.stream()
            .map(this::convertToProjection)
            .collect(Collectors.toList());

        return new PageImpl<>(projections, pageable, total);
    }

    private long getTotalCount(Specification<Property> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Property> root = cq.from(Property.class);
        cq.select(cb.countDistinct(root)).where(spec.toPredicate(root, cq, cb));
        return entityManager.createQuery(cq).getSingleResult();
    }

    private PropertySearchProjection convertToProjection(Property property) {
        return PropertySearchProjection.builder()
            .propertyId(property.getPropertyId())
            .title(property.getTitle())
            .propertyForRent(property.getPropertyForRent() != null ?
                new PropertyForRentProjection(property.getPropertyForRent().getRentalPrice()) : null)
            .propertyForSale(property.getPropertyForSale() != null ?
                new PropertyForSaleProjection(property.getPropertyForSale().getSalePrice()) : null)
            .rooms(property.getRooms().stream()
                .map(room -> new RoomSearchProjection(room.getRoomType().getName(), room.getQuantity()))
                .collect(Collectors.toList()))
            .categoryId(property.getCategoryId())
            .area(property.getArea())
            .view(property.getView())
            .description(property.getDescription())
            .totalFloor(property.getTotalFloor())
            .status(property.getStatus())
            .address(property.getAddress())
            .imageUrls(property.getImageUrls())
            .legalDocument(property.getLegalDocument())
            .furniture(property.getFurniture())
            .createdDate(property.getCreatedDate())
            .build();
    }
}
