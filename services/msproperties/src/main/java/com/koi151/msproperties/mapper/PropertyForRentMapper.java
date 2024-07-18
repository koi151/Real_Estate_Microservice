package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyForRentEntity;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyForRentMapper {
    @Mapping(target = "propertyId", ignore = true)  // Since propertyId will be set in PropertyEntity
    @Mapping(target = "propertyEntity", ignore = true)
    PropertyForRentEntity toPropertyForRentEntity(PropertyForRentCreateRequest propertyForRentCreateRequest);
}
