package com.koi151.msproperties.repository.custom;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepositoryCustom {
    @Query(value = " SELECT p.* FROM property p  " +
            "  [JOIN_CLAUSE]  " +  // Placeholder for JOIN logic
            "  WHERE 1=1  " +
            "  [WHERE_CLAUSE]  " +  // Placeholder for WHERE logic
            "  GROUP BY p.id ",
            nativeQuery = true)
    List<PropertyEntity> findPropertiesByCriteria(PropertySearchRequest request);
}