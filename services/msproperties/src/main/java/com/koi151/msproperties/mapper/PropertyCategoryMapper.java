package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyCategory;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyCategoryMapper {
    PropertyCategoryHomeDTO toPropertyCategoryHomeDTO(PropertyCategory entity);

}
