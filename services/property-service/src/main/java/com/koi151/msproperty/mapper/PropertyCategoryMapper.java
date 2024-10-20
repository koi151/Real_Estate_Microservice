package com.koi151.msproperty.mapper;

import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyCategoryMapper {
    PropertyCategoryHomeDTO toPropertyCategoryHomeDTO(PropertyCategory entity);

}
