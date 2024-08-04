package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.*;
import com.koi151.msproperties.model.projection.PropertyPostServiceProjection;
import com.koi151.msproperties.model.projection.PropertySearchProjection;
import com.koi151.msproperties.model.projection.RoomSearchProjection;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.CustomRepositoryUtils;
import com.koi151.msproperties.utils.QueryContext.QueryContext;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {
    @PersistenceContext // used to inject an EntityManager into a class
    private EntityManager entityManager;

    private static void applyPriceFilters(PropertySearchRequest request, QueryContext<Property> context) {
        // Check if any price filtering is actually needed
        if (request.propertyForSale() != null &&
                (request.propertyForSale().priceFrom() != null || request.propertyForSale().priceTo() != null)
        ) {
            // Sale price filtering is needed
            Join<Property, ?> saleJoin = context.getJoins().get("propertyForSale");
            if (saleJoin != null) {
                CriteriaBuilder cb = context.criteriaBuilder();
                List<Predicate> predicates = context.predicates();

                if (request.propertyForSale().priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(saleJoin.get("salePrice"), request.propertyForSale().priceFrom()));
                }
                if (request.propertyForSale().priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(saleJoin.get("salePrice"), request.propertyForSale().priceTo()));
                }
            }
        }

        if (request.propertyForRent() != null &&
                (request.propertyForRent().priceFrom() != null || request.propertyForRent().priceTo() != null)
        ) {
            // Rent price filtering is needed
            Join<Property, ?> rentJoin = context.getJoins().get("propertyForRent");
            if (rentJoin != null) {
                CriteriaBuilder cb = context.criteriaBuilder();
                List<Predicate> predicates = context.predicates();

                if (request.propertyForRent().priceFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(rentJoin.get("rentalPrice"), request.propertyForRent().priceFrom()));
                }
                if (request.propertyForRent().priceTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(rentJoin.get("rentalPrice"), request.propertyForRent().priceTo()));
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
                predicates.add(cb.like(context.joins().get("rentJoin").get("rentalTerm"), "%" + request.term() + "%"));

            } else if (request.propertyForSale() != null) {
                predicates.add(cb.like(context.joins().get("saleJoin").get("saleTerm"), "%" + request.term() + "%"));
            }
        }

        var addressRequest = request.address();
        if (addressRequest != null && addressJoin != null) {
            // address queries
            if (StringUtil.checkString(addressRequest.city())) {
                predicates.add(cb.like(addressJoin.get("city"), "%" + addressRequest.city() + "%"));
            }
            if (StringUtil.checkString(addressRequest.district())) {
                predicates.add(cb.like(addressJoin.get("district"), "%" + addressRequest.district() + "%"));
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
        if (request.propertyForRent() != null) {
            context.addJoin("propertyForRent", context.root().join("propertyForRent", JoinType.LEFT));
        }
        if (request.propertyForSale() != null) {
            context.addJoin("propertyForSale", context.root().join("propertyForSale", JoinType.LEFT));
        }

        // Always need these joins since it require in property response
        context.addJoin("address", context.root().join("address", JoinType.LEFT));
        context.addJoin("rooms", context.root().join("rooms", JoinType.LEFT));
        context.addJoin("propertyPostService", context.root().join("propertyPostService", JoinType.LEFT));
    }

@Override
//@Cacheable(value = "properties", key = "#request.toString() + '-' + #pageable.toString()")
public Page<PropertySearchProjection> findPropertiesByCriteria(PropertySearchRequest request, Pageable pageable) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<Property> root = cq.from(Property.class);
    List<Predicate> predicates = new ArrayList<>();
    Map<String, Join<Property, ?>> joins = new HashMap<>();

    QueryContext<Property> context = new QueryContext<>(cb, cq, root, predicates, joins);

    if (request != null) {
        joinColumns(request, context);

        Set<String> excludedFields = new HashSet<>(Set.of(
                "propertyForRent", "propertyForSale", "propertyCategory", "areaFrom", "areaTo",
                "priceFrom", "priceTo", "address", "rooms"
        ));

        Map<String, QueryFieldOptionEnum> queryOptions = buildQueryOptions(request, excludedFields);
        CustomRepositoryUtils.appendNormalQueryConditions(request, context, queryOptions);
        applySpecialQueryConditions(request, context);
    }

    predicates.add(cb.equal(root.get("deleted"), false));
    cq.where(predicates.toArray(new Predicate[0]));

    if (pageable.getSort().isSorted()) {
        cq.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
    }

    // get existed join
    Join<Property, ?> roomJoin = joins.get("rooms");
    Join<Property, ?> addressJoin = joins.get("address");
    Join<Property, ?> postServiceJoin = joins.get("propertyPostService");

    // Create a projection query
    cq.multiselect(
        root,
        root.get("propertyId").alias("propertyId"),
        root.get("title").alias("title"),
        cb.selectCase(root.get("propertyForSale"))
            .when(cb.isNotNull(root.get("propertyForSale")), PropertyTypeEnum.SALE)
            .when(cb.isNotNull(root.get("propertyForRent")), PropertyTypeEnum.RENT)
            .when(cb.and(cb.isNotNull(root.get("propertyForSale")), cb.isNotNull(root.get("propertyForRent"))), PropertyTypeEnum.RENT_SALE)
            .as(PropertyTypeEnum.class).alias("type"),
        root.get("categoryId").alias("categoryId"),
        root.get("area").alias("area"),
        cb.coalesce(root.get("propertyForRent").get("rentalPrice"), cb.literal(BigDecimal.ZERO)).alias("rentalPrice"),
        cb.coalesce(root.get("propertyForSale").get("salePrice"), cb.literal(BigDecimal.ZERO)).alias("salePrice"),
        root.get("description").alias("description"),
        root.get("totalFloor").alias("totalFloor"),
        root.get("status").alias("status"),
        root.get("availableFrom").alias("availableFrom"),
        addressJoin.alias("address"),
        root.get("imageUrls").alias("imageUrls"),
        roomJoin.get("roomType").alias("roomType"),
        roomJoin.get("quantity").alias("quantity"),
        postServiceJoin.get("postingPackage").alias("postingPackage"),
        postServiceJoin.get("postingDate").alias("postingDate")
    );
    TypedQuery<Tuple> query = entityManager.createQuery(cq);
    query.setHint("jakarta.persistence.loadgraph", entityManager.getEntityGraph("property-with-details"));

    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());
    List<Tuple> resultList = query.getResultList();

    // Group rooms by property ID
    Map<Long, List<RoomSearchProjection>> roomsByPropertyId = new HashMap<>();
    for (Tuple tuple : resultList) {
        Long propertyId = tuple.get("propertyId", Long.class);
        RoomSearchProjection room = new RoomSearchProjection(
                tuple.get("roomType", RoomTypeEnum.class),
                tuple.get("quantity", Short.class)
        );
        roomsByPropertyId.computeIfAbsent(propertyId, k -> new ArrayList<>()).add(room);
    }

    // Combine properties and rooms
    List<PropertySearchProjection> properties = resultList.stream()
            .collect(Collectors.groupingBy(tuple -> tuple.get("propertyId", Long.class)))
            .entrySet().stream()
            .map(entry -> {
                Tuple tuple = entry.getValue().get(0);
                List<RoomSearchProjection> propertyRooms = roomsByPropertyId.getOrDefault(entry.getKey(), new ArrayList<>());

                // Extract PropertyPostService data from the tuple
                PropertyPostServiceProjection postServiceProjection = new PropertyPostServiceProjection(
                    tuple.get("postingPackage", PostingPackageEnum.class)
//                    tuple.get("postingDate", LocalDateTime.class)
                );

                return PropertySearchProjection.builder()
                    .propertyId(tuple.get("propertyId", Long.class))
                    .title(tuple.get("title", String.class))
                    .type(tuple.get("type", PropertyTypeEnum.class))
                    .categoryId(tuple.get("categoryId", Integer.class))
                    .area(tuple.get("area", BigDecimal.class))
                    .rentalPrice(tuple.get("rentalPrice", BigDecimal.class))
                    .salePrice(tuple.get("salePrice", BigDecimal.class))
                    .description(tuple.get("description", String.class))
                    .totalFloor(tuple.get("totalFloor", Short.class))
                    .status(tuple.get("status", StatusEnum.class))
//                    .availableFrom(tuple.get("availableFrom", LocalDate.class))
                    .address(tuple.get("address", Address.class))
                    .imageUrls(tuple.get("imageUrls", String.class))
                    .rooms(propertyRooms)
                    .propertyPostService(postServiceProjection)
                    .build();
            })
            .collect(Collectors.toList());

    return new PageImpl<>(properties, pageable, properties.size());
}

    // Helper method to build query options
    private Map<String, QueryFieldOptionEnum> buildQueryOptions(PropertySearchRequest request, Set<String> excludedFields) {
        Map<String, QueryFieldOptionEnum> queryOptions = new HashMap<>();
        for (Field field : request.getClass().getDeclaredFields()) {
            var type = field.getType();
            String fieldName = field.getName();
            if (!excludedFields.contains(fieldName)) {
                if (type == String.class) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.LIKE_STRING);
                } else if (Number.class.isAssignableFrom(type)) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.NUMBER);
                } else if (field.getType().isEnum()) {
                    queryOptions.put(field.getName(), QueryFieldOptionEnum.ENUM);
                }
            }
        }
        return queryOptions;
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




