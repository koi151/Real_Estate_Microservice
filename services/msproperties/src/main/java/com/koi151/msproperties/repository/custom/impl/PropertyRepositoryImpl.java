package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.CustomRepositoryUtils;
import com.koi151.msproperties.utils.QueryContext.QueryConditionContextProperty;
import com.koi151.msproperties.utils.RequestUtil;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {
    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static void applyPriceFilters(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();

        Join<Property, ?> saleJoin = context.joins().get("propertyForSale");
        Join<Property, ?> rentJoin = context.joins().get("propertyForRent");

        if (request.type() != null) {
            if (request.type() == PropertyTypeEnum.SALE) {
                if (request.priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.priceFrom()));
                }
                if (request.priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.priceTo()));
                }
            } else if (request.type() == PropertyTypeEnum.RENT) {
                if (request.priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceFrom()));
                }
                if (request.priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceTo()));
                }
            }
        } else {
            if (request.priceFrom() != null) {
                Predicate rentalPricePredicate = null;
                Predicate salePricePredicate = null;

                if (rentJoin != null) {
                    rentalPricePredicate = cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceFrom());
                }
                if (saleJoin != null) {
                    salePricePredicate = cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.priceFrom());
                }

                if (rentalPricePredicate != null && salePricePredicate != null) {
                    predicates.add(cb.or(rentalPricePredicate, salePricePredicate));
                } else if (rentalPricePredicate != null) {
                    predicates.add(rentalPricePredicate);
                } else if (salePricePredicate != null) {
                    predicates.add(salePricePredicate);
                }
            }
            if (request.priceTo() != null) {
                Predicate rentalPricePredicate = null;
                Predicate salePricePredicate = null;

                if (rentJoin != null) {
                    rentalPricePredicate = cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceTo());
                }
                if (saleJoin != null) {
                    salePricePredicate = cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.priceTo());
                }

                if (rentalPricePredicate != null && salePricePredicate != null) {
                    predicates.add(cb.or(rentalPricePredicate, salePricePredicate));
                } else if (rentalPricePredicate != null) {
                    predicates.add(rentalPricePredicate);
                } else if (salePricePredicate != null) {
                    predicates.add(salePricePredicate);
                }
            }
        }
    }

    private static void addRoomConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        addRoomCondition("bedroom", request.bedrooms(), context);
        addRoomCondition("bathroom", request.bathrooms(), context);
        addRoomCondition("kitchen", request.kitchens(), context);
    }

    private static void addRoomCondition(String roomType, Short quantity, QueryConditionContextProperty context) {
        if (quantity != null) {
            CriteriaBuilder cb = context.criteriaBuilder();
            List<Predicate> predicates = context.predicates();

            Join<Property, ?> roomJoin = context.joins().get("rooms");

            predicates.add(cb.and(
                    cb.equal(roomJoin.get("roomType"), roomType),
                    cb.equal(roomJoin.get("quantity"), quantity)
            ));
        }
    }

    private static void applySpecialQueryConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();
        Root<Property> root = context.root();

        Join<?, ?> addressJoin = context.joins().get("address");

        if (request.areaFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("area"), request.areaFrom()));
        if (request.areaTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("area"), request.areaTo()));

        if (request.paymentSchedule() != null)
            predicates.add(cb.equal(context.joins().get("rentJoin").get("paymentSchedule"), request.paymentSchedule()));

        if (StringUtil.checkString(request.term())) {
            if (request.type().isPropertyForRent()) {
                predicates.add(cb.like(context.joins().get("rentJoin").get("rentalTerm"),  "%" + request.term() + "%"));

            } else if (request.type().isPropertyForSale()) {
                predicates.add(cb.like(context.joins().get("rentJoin").get("saleTerm"),  "%" + request.term() + "%"));

            }
        }

        var addressRequest = request.addressSearchRequest();
        // address queries
        if (StringUtil.checkString(addressRequest.city())) {
            predicates.add(cb.like(addressJoin.get("city"),"%" + addressRequest.city() + "%"));
        }
        if (StringUtil.checkString(addressRequest.district())) {
            predicates.add(cb.like(addressJoin.get("district"),"%" + addressRequest.district() + "%"));
        }
        if (StringUtil.checkString(addressRequest.ward())) {
            predicates.add(cb.like(addressJoin.get("ward"), "%" + addressRequest.ward() + "%"));
        }
        if (StringUtil.checkString(addressRequest.streetAddress())) {
            predicates.add((cb.like(addressJoin.get("streetAddress"), "%" + addressRequest.streetAddress() + "%")));
        }

        addRoomConditions(request, context);
        applyPriceFilters(request, context);
    }

    public static void joinColumns(PropertySearchRequest request, QueryConditionContextProperty context) {
        // Using LEFT JOIN ensures that all properties are included in the search results, even if they do not have associated sale, rental, ..etc records

        if (request.type() == null) {
            context.addJoin("propertyForSale", context.root().join("propertyForSale", JoinType.LEFT));
            context.addJoin("propertyForRent", context.root().join("propertyForRent", JoinType.LEFT));
        } else if (request.type().isPropertyForRent()) {
            context.addJoin("propertyForRent", context.root().join("propertyForRent", JoinType.LEFT));
        } else if (request.type().isPropertyForSale()) {
            context.addJoin("propertyForSale", context.root().join("propertyForSale", JoinType.LEFT));
        }

        if (RequestUtil.locationRequested(request)) {
            context.addJoin("address", context.root().join("address", JoinType.LEFT));
        }

        if (RequestUtil.roomRequested(request)) {
            context.addJoin("rooms", context.root().join("rooms", JoinType.LEFT));
        }
    }

    @Override
    public Page<Property> findPropertiesByCriteria(PropertySearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // API in JPA for creating dynamic query
        CriteriaQuery<Property> cq = cb.createQuery(Property.class); // call method from EntityManager to get CriteriaQuery for building queries
        Root<Property> root = cq.from(Property.class); // define PropertyEntity as root entity for queries

        Map<String, Join<Property, ?>> joins = new HashMap<>();
        List<Predicate> predicates = new ArrayList<>(); // interface in Criteria, used for building filter conditions for Criteria queries

        QueryConditionContextProperty context = new QueryConditionContextProperty(cb, cq, root, predicates, joins);

        if (request != null) {
            joinColumns(request, context);

            // Apply normal query condition
            Set<String> excludedFields = new HashSet<>(Set.of(
                    "propertyType", "type", "area", "price", "paymentSchedule", "term",
                    "city", "district", "ward", "address", "kitchens", "bedrooms", "bathrooms"
            ));
            CustomRepositoryUtils.appendNormalQueryConditions(request, root, cb, predicates, excludedFields);

            applySpecialQueryConditions(request, context);
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Apply Sorting
        if (pageable.getSort().isSorted()) {
            cq.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb)); // Use QueryUtils for converting Pageable sort to Order
        }

        TypedQuery<Property> typedQuery = entityManager.createQuery(cq);
        return CustomRepositoryUtils.applyPagination(typedQuery, pageable);
    }
}


//  JDBC with JPA
//
//@Repository
//@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
////@Component("specialCustomImpl")
//public class PropertyRepositoryImpl implements PropertyRepositoryCustom {
//
//    @PersistenceContext // used to inject an EntityManager into a class
//    private EntityManager entityManager;
//
//    private static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
//        return excludedFields.contains(fieldName) ||
//                excludedFields.stream().anyMatch(fieldName::startsWith);
//    }
//
//    private static void appendNormalQueryCondition(Field item, Object value, StringBuilder where) {
//        String fieldName = StringUtil.camelCaseToUnderScore(item.getName());
//        String dataTypeName = item.getType().getName();
//
//        if (dataTypeName.equals("java.lang.Long") || dataTypeName.equals("java.lang.Integer") || dataTypeName.equals("java.lang.Float")) {
//            where.append(" AND p.").append(fieldName).append(" = ").append(value);
//        } else if (dataTypeName.equals("java.lang.String")) {
//            where.append(" AND p.").append(fieldName).append(" LIKE '%").append(value).append("%' ");
//        } else if (value.getClass().isEnum()) {
//            where.append(" AND p.").append(fieldName).append(" = '").append(((Enum<?>) value).name()).append("' ");
//        }
//    }
//
//    public static void applyPriceFilters(PropertySearchRequest request, StringBuilder where) {
//        if (request.getType() != null) {
//            String priceField = request.getType() == PropertyTypeEnum.SALE ? "pfs.sale_price" : "pfr.rental_price";
//            if (request.getPriceFrom() != null) {
//                where.append(" AND ").append(priceField).append(" >= ").append(request.getPriceFrom());
//            }
//            if (request.getPriceTo() != null) {
//                where.append(" AND ").append(priceField).append(" <= ").append(request.getPriceTo());
//            }
//        }
//    }
//
//    public static void queryNormal(PropertySearchRequest request, StringBuilder where) {
//
//        Set<String> excludedFields = new HashSet<>(Set.of( // using Set for quick lookup
//                "propertyType", "type", "area", "price", "paymentSchedule", "term",
//                "city", "district", "ward", "address", "kitchens", "bedrooms", "bathrooms"
//        ));
//
//        Field[] fields = PropertySearchRequest.class.getDeclaredFields();
//
//        for (Field item : fields) {
//            try {
//                item.setAccessible(true); // allow access to private fields
//
//                if (!isExcludedField(item.getName(), excludedFields)) { // skip field that for querySpecial
//                    Object value = item.get(request);
//                    if (value != null && !value.toString().isEmpty())
//                        appendNormalQueryCondition(item, value, where);
//                }
//            } catch (IllegalAccessException ex) {
//                System.err.println("Error accessing field value: " + ex.getMessage());
//            }
//        }
//    }
//
//    public static void querySpecial(PropertySearchRequest request, StringBuilder where) {
//        Float areaTo = request.getAreaTo();
//        Float areaFrom = request.getAreaFrom();
//        PaymentScheduleEnum paymentSchedule= request.getPaymentSchedule();
//        PropertyTypeEnum type = request.getType();
//        String term = request.getTerm();
//
//        if (areaFrom != null)
//            where.append(" AND p.area >= ").append(areaFrom);
//        if (areaTo != null)
//            where.append(" AND p.area <= ").append(areaTo);
//
//        if (paymentSchedule != null) // enum
//            where.append(" AND pfr.payment_schedule = '").append(paymentSchedule).append("'");
//
//        if (StringUtil.checkString(term)) { // only query term when property type defined
//            where.append(" AND ")
//                    .append(type == PropertyTypeEnum.RENT ? "pfr.rental_term" :
//                            type == PropertyTypeEnum.SALE ? "pfs.sale_term" : null)
//                    .append(" LIKE '%").append(term).append("%'");
//        }
//
//        // address
//        if (StringUtil.checkString(request.getCity()))
//            where.append(" AND ad.city = '").append(request.getCity()).append("'");
//        if (StringUtil.checkString(request.getDistrict()))
//            where.append(" AND ad.district = '").append(request.getDistrict()).append("'");
//        if (StringUtil.checkString(request.getWard()))
//            where.append(" AND ad.ward = '").append(request.getWard()).append("'");
//        if (StringUtil.checkString(request.getAddress()))
//            where.append(" AND ad.street_address LIKE '%").append(request.getAddress()).append("%'");
//
//        // rooms
//        if (request.getBedrooms() != null)
//            where.append(" AND r.room_type LIKE '%bedroom%' AND quantity = ").append(request.getBedrooms());
//        if (request.getBathrooms() != null)
//            where.append(" AND r.room_type LIKE '%bathroom%' AND quantity = ").append(request.getBathrooms());
//        if (request.getKitchens() != null)
//            where.append(" AND r.room_type LIKE '%kitchen%' AND quantity = ").append(request.getKitchens());
//
//        applyPriceFilters(request, where);
//    }
//
//    public static void joinTable(PropertySearchRequest request, StringBuilder sql) {
//        PropertyTypeEnum propertyType = request.getType();
//
//        // category
//        if (request.getCategoryId() != null)
//            sql.append(" INNER JOIN property_category pc ON p.id = pc.category_id ");
//
//        // property_for_rent && property_for_sale
//        if (propertyType == PropertyTypeEnum.RENT || request.getPaymentSchedule() != null) { // payment schedule filtering only available with property for rent
//            sql.append(" INNER JOIN property_for_rent pfr ON p.id = pfr.property_entity_id ");
//        } else if (propertyType == PropertyTypeEnum.SALE) {
//            sql.append(" INNER JOIN property_for_sale pfs ON p.id = pfs.property_entity_id ");
//        }
//
//        // address
//        if (StringUtil.checkString(request.getCity()) || StringUtil.checkString(request.getDistrict())
//                || StringUtil.checkString(request.getWard()) || StringUtil.checkString(request.getAddress())) {
//            sql.append(" INNER JOIN address ad ON p.address_id = ad.id");
//        }
//
//        // rooms
//        if (request.getBedrooms() != null || request.getBathrooms() != null || request.getKitchens() != null)
//            sql.append(" INNER JOIN room r ON p.id = r.property_id ");
//    }
//
//    @Override
//    public List<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request) {
//                StringBuilder sql = new StringBuilder(" SELECT p.* FROM property p ");
//                joinTable(request, sql);
//                StringBuilder where = new StringBuilder(" WHERE 1=1 ");
//                queryNormal(request, where);
//                querySpecial(request, where);
//                where.append(" GROUP BY p.id ");
//                sql.append(where);
//
//                System.out.println("sql: " + sql);
//
//                Query query = entityManager.createNativeQuery(sql.toString(), PropertyEntity.class);
//                return query.getResultList();
//        }
//}




