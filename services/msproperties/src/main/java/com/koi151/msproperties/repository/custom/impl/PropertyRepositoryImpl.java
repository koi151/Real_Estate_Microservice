package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.QueryFieldOptionEnum;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.CustomRepositoryUtils;
import com.koi151.msproperties.utils.QueryContext.QueryContext;
import com.koi151.msproperties.utils.RequestUtil;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {
    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(PropertyRepositoryImpl.class);

    private static void applyPriceFilters(PropertySearchRequest request, QueryContext<Property> context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();

        Join<Property, ?> saleJoin = context.joins().get("propertyForSale");
        Join<Property, ?> rentJoin = context.joins().get("propertyForRent");

        if (saleJoin != null || rentJoin != null) {
            if (saleJoin != null) {
                if (request.priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.priceFrom()));
                }
                if (request.priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.priceTo()));
                }
            } else { // rentJoin != null
                if (request.priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceFrom()));
                }
                if (request.priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.priceTo()));
                }
            }
        }
    }

    private static void addRoomConditions(PropertySearchRequest request, QueryContext<Property> context) {
        if (request.rooms() != null && !request.rooms().isEmpty()) {
            CriteriaBuilder cb = context.criteriaBuilder();
            Join<Property, ?> roomJoin = context.joins().get("rooms");

            request.rooms().forEach(roomReq -> context.predicates().add(
                cb.and(
                    cb.equal(roomJoin.get("roomType"), roomReq.roomType()),
                    cb.equal(roomJoin.get("quantity"), roomReq.quantity())
                )
            ));
        }
    }

    private static void applySpecialQueryConditions(PropertySearchRequest request, QueryContext<Property> context) {
        List<Predicate> predicates = context.predicates();
        CriteriaBuilder cb = context.criteriaBuilder();
        Root<Property> root = context.root();

        Join<?, ?> addressJoin = context.joins().get("address");

        if (request.areaFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("area"), request.areaFrom()));
        if (request.areaTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("area"), request.areaTo()));

        if (request.propertyForRent() != null && request.propertyForRent().paymentSchedule() != null)
            predicates.add(cb.equal(context.joins().get("rentJoin").get("paymentSchedule"), request.propertyForRent().paymentSchedule()));

        if (StringUtil.checkString(request.term())) {
            if (request.propertyForRent() != null) {
                predicates.add(cb.like(context.joins().get("rentJoin").get("rentalTerm"),  "%" + request.term() + "%"));

            } else if (request.propertyForSale() != null) {
                predicates.add(cb.like(context.joins().get("saleJoin").get("saleTerm"),  "%" + request.term() + "%"));
            }
        }

        var addressRequest = request.address();
        if (addressRequest != null && addressJoin != null) {
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
        }

        addRoomConditions(request, context);
        applyPriceFilters(request, context);
    }

    public static void joinColumns(PropertySearchRequest request, QueryContext<Property> context) {
        // Using LEFT JOIN ensures that all properties are included in the search results, even if they do not have associated sale, rental, ..etc records

        if (request.propertyForRent() == null && request.propertyForSale() == null ) {
            context.addJoin("propertyForSale", context.root().join("propertyForSale", JoinType.LEFT));
            context.addJoin("propertyForRent", context.root().join("propertyForRent", JoinType.LEFT));
        } else if (request.propertyForRent() != null) {
            context.addJoin("propertyForRent", context.root().join("propertyForRent", JoinType.LEFT));
        } else {
            context.addJoin("propertyForSale", context.root().join("propertyForSale", JoinType.LEFT));
        }

        if (RequestUtil.locationRequested(request)) {
            context.addJoin("address", context.root().join("address", JoinType.LEFT));
        }

        if (request.rooms() != null && !request.rooms().isEmpty()) {
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

        QueryContext<Property> context = new QueryContext<>(cb, cq, root, predicates, joins);

        // Exclude specific fields
        Set<String> excludedFields = new HashSet<>(Set.of( // Exclude fields that for special query method handling
                "propertyForRent", "propertyForSale", "propertyCategory", "areaFrom", "areaTo",
                "priceFrom", "priceTo", "address", "rooms"
        ));

        // Apply default query options (LIKE for Strings, EXACT for others)
        Map<String, QueryFieldOptionEnum> queryOptions = new HashMap<>();
        for (Field field : request.getClass().getDeclaredFields()) { // can seperated into other method
            var type = field.getType();
            String fieldName = field.getName();
            if (!excludedFields.contains(fieldName)) { // excluded fields for special query
                if (type == String.class) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.LIKE_STRING);
                } else if (Number.class.isAssignableFrom(type)) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.NUMBER);
                } else if (field.getType().isEnum()) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.ENUM);
                }
            }
        }
        // Join columns
        joinColumns(request, context);

        // Append normal query
        CustomRepositoryUtils.appendNormalQueryConditions(request, context, queryOptions);

        // Append special query (complex)
        applySpecialQueryConditions(request, context);

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Apply Sorting
        if (pageable.getSort().isSorted()) {
            cq.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb)); // Use QueryUtils for converting Pageable sort to Order
        }

        TypedQuery<Property> typedQuery = entityManager.createQuery(cq);

        /*
           Use EntityGraph to specify related entities that should be fetched along with the main entity
           If not, accessing data in service layer -> trigger lazy loading -> n+1 query problem
        * */
        typedQuery.setHint("jakarta.persistence.loadgraph", entityManager.getEntityGraph("Property.search"));
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




