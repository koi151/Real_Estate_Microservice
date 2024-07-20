package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyForRent;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyForRentMapper {
    @Mapping(target = "propertyId", ignore = true)  // Since propertyId will be set in PropertyEntity
    @Mapping(target = "property", ignore = true)
    PropertyForRent toPropertyForRentEntity(PropertyForRentCreateRequest propertyForRentCreateRequest);
}
