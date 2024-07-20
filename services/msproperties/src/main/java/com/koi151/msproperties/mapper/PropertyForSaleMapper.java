package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyForSale;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyForSaleMapper {
    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "property", ignore = true)
    PropertyForSale toPropertyForSaleEntity(PropertyForSaleCreateRequest propertyForSaleCreateRequest);
}
