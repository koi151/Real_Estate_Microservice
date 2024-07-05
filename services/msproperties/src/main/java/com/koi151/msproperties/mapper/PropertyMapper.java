package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyMapper INSTANCE = Mappers.getMapper( PropertyMapper.class );

//    @Mapping(target = "imageUrls", ignore = true)
//    @Mapping(target = "propertyForRentEntity", ignore = true)
//    @Mapping(target = "propertyForSaleEntity", ignore = true)
//    @Mapping(target = "roomEntities", ignore = true)
    PropertyEntity toPropertyEntity(PropertyCreateRequest request);
}


