package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.AddressEntity;
import com.koi151.msproperties.model.request.AddressCreateRequest;
import com.koi151.msproperties.model.request.AddressUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper( AddressMapper.class );

    AddressEntity toAddressEntity(AddressCreateRequest addressCreateRequest);

    AddressEntity updateAddressEntity(AddressUpdateRequest addressCreateRequest);
}