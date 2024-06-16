package com.koi151.msproperties.repository.custom.impl;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.request.PropertySearchRequest;

import com.koi151.msproperties.repository.custom.PropertyRepositoryCustom;
import com.koi151.msproperties.utils.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public static void queryNormal(PropertySearchRequest propertySearchRequest, StringBuilder where) {
        Field[] fields = PropertySearchRequest.class.getDeclaredFields();

        for (Field item : fields) {
            try {
                item.setAccessible(true); // allow access to private fields
                String fieldName = StringUtil.camelCaseToUnderScore(item.getName());

                if (!fieldName.equals("categoryId") && !fieldName.startsWith("area") // skip field that for querySpecial
                        && !fieldName.startsWith("price") && !fieldName.equals("propertyType")) {
                    Object value = item.get(propertySearchRequest);

                    if (value != null && !value.toString().isEmpty())
                        appendNormalQueryCondition(item, value, where);
                }
            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value: " + ex.getMessage());
            }
        }
    }


    private static void appendNormalQueryCondition(Field item, Object value, StringBuilder where) {
        String fieldName = StringUtil.camelCaseToUnderScore(item.getName());
        String dataTypeName = item.getType().getName();

        if (dataTypeName.equals("java.lang.Long") || dataTypeName.equals("java.lang.Integer") || dataTypeName.equals("java.lang.Float")) {
            where.append(" AND p.").append(fieldName).append(" = ").append(value);
        } else if (dataTypeName.equals("java.lang.String")) {
            where.append(" AND p.").append(fieldName).append(" LIKE '%").append(value).append("%'");
        } else if (value.getClass().isEnum()) {
            where.append(" AND p.").append(fieldName).append(" = '").append(((Enum<?>) value).name()).append("'");
        }
    }

    public static void applyPriceFilters(PropertySearchRequest request, StringBuilder where) {
        if (request.getType() != null) {
            String priceField = request.getType() == PropertyTypeEnum.SALE ? "pfs.sale_price" : "pfr.rental_price";
            if (request.getPriceFrom() != null) {
                where.append(" AND ").append(priceField).append(" >= ").append(request.getPriceFrom());
            }
            if (request.getPriceTo() != null) {
                where.append(" AND ").append(priceField).append(" <= ").append(request.getPriceTo());
            }
        }
    }

    public static void querySpecial(PropertySearchRequest request, StringBuilder where) {

        Float areaTo = request.getAreaTo();
        Float areaFrom = request.getAreaFrom();

        if (areaFrom != null)
            where.append(" AND p.area >= ").append(areaFrom);
        if (areaTo != null)
            where.append(" AND p.area <= ").append(areaTo);

        applyPriceFilters(request, where);
    }

    public static void joinTable(PropertySearchRequest propertySearchRequest, StringBuilder sql) {
        Integer categoryId = propertySearchRequest.getCategoryId();
        PropertyTypeEnum propertyType = propertySearchRequest.getType();

        if (categoryId != null) {
            sql.append(" INNER JOIN property_category pc ON p.id = pc.property_id ");
        }

        if (propertyType == PropertyTypeEnum.SALE) {
            sql.append(" INNER JOIN property_for_sale pfs ON p.id = pfs.property_entity_id ");
        } else if (propertyType == PropertyTypeEnum.RENT) {
            sql.append(" INNER JOIN property_for_rent pfr ON p.id = pfr.property_entity_id ");
        }
    }

    @Override
    public List<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request) {
                StringBuilder sql = new StringBuilder(" SELECT p.* FROM property p ");
                joinTable(request, sql);
                StringBuilder where = new StringBuilder(" WHERE 1=1 ");
                queryNormal(request, where);
                querySpecial(request, where);
                where.append(" GROUP BY p.id ");
                sql.append(where);

                System.out.println("sql: " + sql);

                Query query = entityManager.createNativeQuery(sql.toString(), PropertyEntity.class);
                return query.getResultList();
        }
}


