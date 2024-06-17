package com.koi151.mspropertycategory.repository.custom.impl;


import com.koi151.mspropertycategory.entity.PropertyCategory;
import com.koi151.mspropertycategory.model.request.PropertyCategorySearchRequest;
import com.koi151.mspropertycategory.repository.custom.PropertyCategoryRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Repository
@Primary // need this to use custom repository instead of Spring Data JPA automatically generate queries based on method names
public class PropertyCategoryRepositoryImpl implements PropertyCategoryRepositoryCustom {

    @PersistenceContext // used to inject an EntityManager into a class
    EntityManager entityManager;

    public void joinTable(PropertyCategorySearchRequest request, StringBuilder sql) {
    }

    public static boolean isExcludedField(String fieldName, Set<String> excludedFields) {
        return excludedFields.contains(fieldName) ||
                excludedFields.stream().anyMatch(fieldName::startsWith);
    }

    public void appendNormalQueryCondition(Field item, Object value, StringBuilder where) {
        String fieldName = item.getName();
        String dataTypeName = item.getType().getName();

        if (dataTypeName.equals("java.lang.Integer") || dataTypeName.equals("java.lang.Long") || dataTypeName.equals("java.lang.Float")) {
            where.append(" AND pc.").append(fieldName).append(" = ").append(value);
        } else if (dataTypeName.equals("java.lang.String")) {
            where.append(" AND pc.").append(fieldName).append(" LIKE '%").append(value).append("%' ");
        } else if (value.getClass().isEnum()) {
            where.append(" AND pc.").append(fieldName).append(" = '").append(((Enum<?>) value).name()).append("' ");
        }
    }

    public void queryNormal(PropertyCategorySearchRequest request, StringBuilder where) {
        Set<String> excludedFields = Set.of(""); // add when needed

        Field[] fields = PropertyCategorySearchRequest.class.getDeclaredFields();
        for (Field item : fields) {
            try {
                item.setAccessible(true); // allow access to private fields
                if (!isExcludedField(item.getName(), excludedFields)) {
                    Object value = item.get(request);
                    if (value != null && !value.toString().isEmpty())
                        appendNormalQueryCondition(item, value, where);
                }

            } catch (IllegalAccessException ex) {
                System.err.println("Error accessing field value " + ex.getMessage());
            }
        }
    }

//    public static void querySpecial(PropertyCategorySearchRequest request, StringBuilder where) {
//    }

    @Override
    public List<PropertyCategory> getPropertyCategoryByCriterias(PropertyCategorySearchRequest request) {
        StringBuilder sql = new StringBuilder(" SELECT pc.* from property_category pc ");
        joinTable(request, sql);
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        queryNormal(request, where);
//        querySpecial(request where);
        where.append(" GROUP BY p.id ");
        sql.append(where);

        System.out.println("sql: " + sql);


        return null;
    }
}
