package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyCategoryEntity;
import com.koi151.msproperties.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyCategoryMapper {
    PropertyCategoryHomeDTO toPropertyCategoryHomeDTO(PropertyCategoryEntity entity);

}
