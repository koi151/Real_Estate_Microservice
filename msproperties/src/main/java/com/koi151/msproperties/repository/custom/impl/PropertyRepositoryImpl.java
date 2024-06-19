package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.QueryConditionContextProperty;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
        return excludedFields.contains(fieldName) ||
                excludedFields.stream().anyMatch(fieldName::startsWith);
    }

    private static void appendNormalQueryConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        Set<String> excludedFields = new HashSet<>(Set.of(
                "propertyType", "type", "area", "price", "paymentSchedule", "term",
                "city", "district", "ward", "address", "kitchens", "bedrooms", "bathrooms"
        ));

        Field[] fields = PropertySearchRequest.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(request);

                if (!isExcludedField(fieldName, excludedFields) && value != null && !value.toString().isEmpty()) {
                    Path<?> path = context.root().get(fieldName); // represent for one specific attribute of entity
                    if (value instanceof Integer || value instanceof Float || value instanceof Long) {
                        context.predicates().add(context.criteriaBuilder().equal(path, value));
                    } else if (value instanceof String) {
                        context.predicates().add(context.criteriaBuilder().like(path.as(String.class), "%" + value + "%"));
                    } else if (value.getClass().isEnum()) {
                        context.predicates().add(context.criteriaBuilder().equal(path, ((Enum<?>) value).name()));
                    }
                }

            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value: " + ex.getMessage());
            }
        }
    }

    private static void applyPriceFilters(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();

        Join<PropertyEntity, ?> saleJoin = context.joins().get("propertyForSaleEntity");
        Join<PropertyEntity, ?> rentJoin = context.joins().get("propertyForRentEntity");

        if (request.getType() != null) {
            if (request.getType() == PropertyTypeEnum.SALE) {
                if (request.getPriceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.getPriceFrom()));
                }
                if (request.getPriceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.getPriceTo()));
                }
            } else if (request.getType() == PropertyTypeEnum.RENT) {
                if (request.getPriceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceFrom()));
                }
                if (request.getPriceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceTo()));
                }
            }
        } else {
            // When type is null, apply price filters using OR logic
//            Predicate rentalPricePredicate = null;
//            Predicate salePricePredicate = null;

//            predicates.add(cb.and(
//                    cb.equal(roomJoin.get("roomType"), roomType),
//                    cb.equal(roomJoin.get("quantity"), quantity)
//            ));

            if (request.getPriceFrom() != null) { // currently for both rent & sale properties (both tables joined property)
                if (rentJoin != null) {
//                    rentalPricePredicate = cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceFrom());
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceFrom()));
                }
                if (saleJoin != null) {
//                    salePricePredicate = cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.getPriceFrom());
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("salePrice"), request.getPriceFrom()));
                }

            }
            if (request.getPriceTo() != null) {
                if (rentJoin != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceFrom()));

//                    Predicate rentalPriceToPredicate = cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.getPriceTo());
//                    rentalPricePredicate = (rentalPricePredicate == null) ? rentalPriceToPredicate : cb.and(rentalPricePredicate, rentalPriceToPredicate);
                }

                if (saleJoin != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("salePrice"), request.getPriceFrom()));

//                    Predicate salePriceToPredicate = cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.getPriceTo());
//                    salePricePredicate = (salePricePredicate == null) ? salePriceToPredicate : cb.and(salePricePredicate, salePriceToPredicate);
                }
            }

            // Combine rentalPricePredicate and salePricePredicate with OR
//            if (rentalPricePredicate != null || salePricePredicate != null) {
//                predicates.add(cb.or(rentalPricePredicate, salePricePredicate));
//            }
        }
    }

    private static void addRoomConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        addRoomCondition("bedroom", request.getBedrooms(), context);
        addRoomCondition("bathroom", request.getBathrooms(), context);
        addRoomCondition("kitchen", request.getKitchens(), context);
    }

    private static void addRoomCondition(String roomType, Integer quantity, QueryConditionContextProperty context) {
        if (quantity != null) {
            CriteriaBuilder cb = context.criteriaBuilder();
            List<Predicate> predicates = context.predicates();

            Join<PropertyEntity, ?> roomJoin = context.joins().get("roomEntity");

            predicates.add(cb.and(
                    cb.equal(roomJoin.get("roomType"), roomType),
                    cb.equal(roomJoin.get("quantity"), quantity)
            ));
        }
    }

    private static void applySpecialQueryConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();
        Root<PropertyEntity> root = context.root();

        Join<?, ?> addressJoin = context.joins().get("addressEntity");

        if (request.getAreaFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("area"), request.getAreaFrom()));
        if (request.getAreaTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("area"), request.getAreaTo()));

        if (request.getPaymentSchedule() != null)
            predicates.add(cb.equal(context.joins().get("rentJoin").get("paymentSchedule"), request.getPaymentSchedule()));

        if (StringUtil.checkString(request.getTerm())) {
            if (request.getType().isPropertyForRent()) {
                predicates.add(cb.like(context.joins().get("rentJoin").get("rentalTerm"),  "%" + request.getTerm() + "%"));

            } else if (request.getType().isPropertyForSale()) {
                predicates.add(cb.like(context.joins().get("rentJoin").get("saleTerm"),  "%" + request.getTerm() + "%"));

            }
        }

        if (StringUtil.checkString(request.getCity())) {
            predicates.add(cb.like(addressJoin.get("city"),"%" + request.getCity() + "%"));
        }
        if (StringUtil.checkString(request.getDistrict())) {
            predicates.add(cb.like(addressJoin.get("district"),"%" + request.getDistrict() + "%"));
        }
        if (StringUtil.checkString(request.getWard())) {
            predicates.add(cb.like(addressJoin.get("ward"), "%" + request.getWard() + "%"));
        }
        if (StringUtil.checkString(request.getAddress())) {
            predicates.add((cb.like(addressJoin.get("streetAddress"), "%" + request.getAddress() + "%")));
        }

        addRoomConditions(request, context);
        applyPriceFilters(request, context);
    }

    public static void joinColumns(PropertySearchRequest request, QueryConditionContextProperty context) {
        if (request.getType() == null) {
            context.addJoin("propertyForSaleEntity", context.root().join("propertyForSaleEntity", JoinType.LEFT));
            context.addJoin("propertyForRentEntity", context.root().join("propertyForRentEntity", JoinType.LEFT));
        } else if (request.getType().isPropertyForRent()) {
            context.addJoin("propertyForRentEntity", context.root().join("propertyForRentEntity", JoinType.LEFT));
        } else if (request.getType().isPropertyForSale()) {
            context.addJoin("propertyForSaleEntity", context.root().join("propertyForSaleEntity", JoinType.LEFT));
        }

        if (StringUtil.checkString(request.getCity()) || StringUtil.checkString(request.getDistrict())
            || StringUtil.checkString(request.getWard()) || StringUtil.checkString(request.getAddress())) {
            context.addJoin("addressEntity", context.root().join("addressEntity", JoinType.LEFT));
        }

        if (request.getBedrooms() != null || request.getBathrooms() != null || request.getKitchens() != null) {
            context.addJoin("roomEntity", context.root().join("roomEntity", JoinType.LEFT));
        }
    }



    @Override
    public List<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // API in JPA create dynamic query
        CriteriaQuery<PropertyEntity> cq = cb.createQuery(PropertyEntity.class); // call method from EntityManager to get CriteriaQuery for building queries
        Root<PropertyEntity> root = cq.from(PropertyEntity.class); // define PropertyEntity as root entity for queries

        Map<String, Join<PropertyEntity, ?>> joins = new HashMap<>();
        List<Predicate> predicates = new ArrayList<>(); // interface in Criteria, used for building filter conditions for Criteria queries

        QueryConditionContextProperty context = new QueryConditionContextProperty(cb, cq, root, predicates, joins);

        if (request != null) {
            joinColumns(request, context);
            appendNormalQueryConditions(request, context);
            applySpecialQueryConditions(request, context);
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(cq);
        return typedQuery.getResultList();
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




