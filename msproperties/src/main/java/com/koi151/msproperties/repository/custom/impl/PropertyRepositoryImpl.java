package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.RoomEntity;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.QueryConditionContextProperty;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
        return excludedFields.contains(fieldName) ||
                excludedFields.stream().anyMatch(fieldName::startsWith);
    }

    //     private static void appendNormalQueryConditions(PropertySearchRequest request, CriteriaBuilder cb, Root<PropertyEntity> root, List<Predicate> predicates) {
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
                    Path<?> path = context.getRoot().get(fieldName); // represent for one specific attribute of entity
                    if (value instanceof Integer || value instanceof  Float || value instanceof Long) {
                        context.getPredicates().add(context.getCriteriaBuilder().equal(path, value));
                    } else if (value instanceof String) {
                        context.getPredicates().add(context.getCriteriaBuilder().like(path.as(String.class), "%" + value + "%"));
                    } else if (value.getClass().isEnum()) {
                        context.getPredicates().add(context.getCriteriaBuilder().equal(path, ((Enum<?>) value).name()));
                    }
                }

            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value: " + ex.getMessage());
            }
        }
    }

    private static void applyPriceFilters(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.getPredicates();
        Root<PropertyEntity> root = context.getRoot();
        CriteriaBuilder cb = context.getCriteriaBuilder();

        if (request.getType() != null) {
            if (request.getType() == PropertyTypeEnum.SALE) {
                if (request.getPriceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("propertyForSaleEntity").get("salePrice"), request.getPriceFrom()));
                }
                if (request.getPriceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("propertyForSaleEntity").get("salePrice"), request.getPriceTo()));
                }
            } else if (request.getType() == PropertyTypeEnum.RENT) {
                if (request.getPriceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("propertyForRentEntity").get("rentalPrice"), request.getPriceFrom()));
                }
                if (request.getPriceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("propertyForRentEntity").get("rentalPrice"), request.getPriceTo()));
                }
            }
        }
    }

    private static void addRoomConditions(PropertySearchRequest request, QueryConditionContextProperty context) {
        addRoomCondition("bedroom", request.getBedrooms(), context);
        addRoomCondition("bathroom", request.getBathrooms(), context);
        addRoomCondition("kitchen", request.getKitchens(), context);
    }

    private static void addRoomCondition(String roomType, Integer quantity, QueryConditionContextProperty context) {
        if (quantity != null) {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<PropertyEntity> root = context.getRoot();
            List<Predicate> predicates = context.getPredicates();

            Join<PropertyEntity, RoomEntity> roomJoin = root.join("roomEntities", JoinType.INNER);
            predicates.add(cb.and(
                    cb.equal(roomJoin.get("roomType"), roomType),
                    cb.equal(roomJoin.get("quantity"), quantity)
            ));
        }
    }

    private static void applySpecialFilters(PropertySearchRequest request, QueryConditionContextProperty context) {
        List<Predicate> predicates = context.getPredicates();
        CriteriaBuilder cb = context.getCriteriaBuilder();
        Root<PropertyEntity> root = context.getRoot();

        if (request.getAreaFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("area"), request.getAreaFrom()));
        if (request.getAreaTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("area"), request.getAreaFrom()));


        if (request.getPaymentSchedule() != null)
            predicates.add(cb.equal(root.get("propertyForRentEntity").get("paymentSchedule"), request.getPaymentSchedule()));

        if (StringUtil.checkString(request.getTerm())) {
            if (request.getType() == PropertyTypeEnum.RENT) {
                predicates.add(cb.like(root.get("propertyForRentEntity").get("rentalTerm"), "%" + request.getTerm() + "%"));
            } else if (request.getType() == PropertyTypeEnum.SALE) {
                predicates.add(cb.like(root.get("propertyForSaleEntity").get("saleTerm"), "%" + request.getTerm() + "%"));
            }
        }

        if (StringUtil.checkString(request.getCity())) {
            predicates.add(cb.equal(root.get("addressEntity").get("city"), request.getCity()));
        }
        if (StringUtil.checkString(request.getDistrict())) {
            predicates.add(cb.equal(root.get("addressEntity").get("district"), request.getDistrict()));
        }
        if (StringUtil.checkString(request.getWard())) {
            predicates.add(cb.equal(root.get("addressEntity").get("ward"), request.getWard()));
        }
        if (StringUtil.checkString(request.getAddress())) {
            predicates.add(cb.like(root.get("addressEntity").get("streetAddress"), "%" + request.getAddress() + "%"));
        }

         addRoomConditions(request, context);
    }

    @Override
    public List<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // API in JPA create dynamic query
        CriteriaQuery<PropertyEntity> cq = cb.createQuery(PropertyEntity.class); // call method from EntityManager to get CriteriaQuery for building queries
        Root<PropertyEntity> root = cq.from(PropertyEntity.class); // define PropertyEntity as root entity for queries

        List<Predicate> predicates = new ArrayList<>(); // interface in Criteria,used for building filter conditions for Criteria queries

        QueryConditionContextProperty context = new QueryConditionContextProperty(cb, cq, root, predicates);

        appendNormalQueryConditions(request, context);
        applyPriceFilters(request, context);
        applySpecialFilters(request, context);

        cq.where(predicates.toArray(new Predicate[0]));

        Query query = entityManager.createQuery(cq);

        return query.getResultList();
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




