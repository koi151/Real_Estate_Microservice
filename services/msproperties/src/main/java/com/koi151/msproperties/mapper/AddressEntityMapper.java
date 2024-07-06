package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.AddressEntity;
import com.koi151.msproperties.model.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressEntityMapper {
    AddressEntityMapper INSTANCE = Mappers.getMapper( AddressEntityMapper.class );

    AddressEntity toAddressEntity(AddressRequest addressRequest);
}