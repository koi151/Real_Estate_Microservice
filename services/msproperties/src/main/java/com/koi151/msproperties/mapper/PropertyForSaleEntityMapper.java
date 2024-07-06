package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyForSaleEntity;
import com.koi151.msproperties.model.request.PropertyForSaleCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyForSaleEntityMapper {
    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "propertyEntity", ignore = true)
    PropertyForSaleEntity toPropertyForSaleEntity(PropertyForSaleCreateRequest propertyForSaleCreateRequest);
}
