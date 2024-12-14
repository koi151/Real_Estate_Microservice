package com.koi151.msproperty.specification;

import com.koi151.msproperty.entity.Property;
import com.koi151.msproperty.model.request.property.PropertyFilterRequest;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.request.rooms.RoomFilterRequest;
import com.koi151.msproperty.utils.StringUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PropertySpecifications {

    public static Specification<Property> buildSpecification(PropertyFilterRequest request, boolean isClientView) {
        return (Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // property type
            if (request.type() != null) {
                if (request.type().isPropertyForRent()) {
                    predicates.add(cb.isNotNull(root.get("propertyForRent")));
                } else if (request.type().isPropertyForSale()) {
                    predicates.add(cb.isNotNull(root.get("propertyForSale")));
                }
            }

            if (request.status() != null) {
                predicates.add(cb.equal(root.get("status"), request.status()));
            } else if (isClientView) {
                predicates.add(cb.equal(root.get("status"), StatusEnum.ACTIVE));
            }

            if (request.categoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), request.categoryId()));
            }

            if (request.areaFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"), request.areaFrom()));
            }

            if (request.areaTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("area"), request.areaTo()));
            }

            if (request.address() != null) {
                Join<Object, Object> address = root.join("address", JoinType.LEFT);
                if (StringUtils.checkString(request.address().city())) {
                    predicates.add(cb.like(cb.lower(address.get("city")), "%" + request.address().city().toLowerCase() + "%"));
                }
                if (StringUtils.checkString(request.address().district())) {
                    predicates.add(cb.like(cb.lower(address.get("district")), "%" + request.address().district().toLowerCase() + "%"));
                }
                if (StringUtils.checkString(request.address().ward())) {
                    predicates.add(cb.like(cb.lower(address.get("ward")), "%" + request.address().ward().toLowerCase() + "%"));
                }
                if (StringUtils.checkString(request.address().streetAddress())) {
                    predicates.add(cb.like(cb.lower(address.get("streetAddress")), "%" + request.address().streetAddress().toLowerCase() + "%"));
                }
            }

            if (request.rooms() != null && !request.rooms().isEmpty()) {
                for (int i = 0; i < request.rooms().size(); i++) {
                    RoomFilterRequest room = request.rooms().get(i);
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Property> subRoot = subquery.from(Property.class);
                    Join<Object, Object> rooms = subRoot.join("rooms", JoinType.LEFT);
                    subquery.select(cb.count(subRoot.get("propertyId")))
                        .where(
                            cb.equal(subRoot.get("propertyId"), root.get("propertyId")),
                            cb.equal(rooms.get("roomType"), room.roomType()),
                            cb.equal(rooms.get("quantity"), room.quantity())
                        );
                    predicates.add(cb.greaterThan(subquery, 0L));
                }
            }

//            if (request.overallPriceFrom() != null || request.overallPriceTo() != null) {
//                Predicate salePricePredicate = cb.conjunction();
//                Predicate rentPricePredicate = cb.conjunction();
//                if (request.overallPriceFrom() != null) {
//                    salePricePredicate = cb.ge(root.join("propertyForSale", JoinType.LEFT).get("salePrice"), request.overallPriceFrom());
//                    rentPricePredicate = cb.ge(root.join("propertyForRent", JoinType.LEFT).get("rentalPrice"), request.overallPriceFrom());
//                }
//                if (request.overallPriceTo() != null) {
//                    salePricePredicate = cb.and(salePricePredicate, cb.le(root.join("propertyForSale", JoinType.LEFT).get("salePrice"), request.overallPriceTo()));
//                    rentPricePredicate = cb.and(rentPricePredicate, cb.le(root.join("propertyForRent", JoinType.LEFT).get("rentalPrice"), request.overallPriceTo()));
//                }
//                predicates.add(cb.or(salePricePredicate, rentPricePredicate));
//            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

