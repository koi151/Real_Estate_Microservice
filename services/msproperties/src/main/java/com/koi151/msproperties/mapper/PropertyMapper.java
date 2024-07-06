package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.model.dto.FullPropertyDTO;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import com.koi151.msproperties.model.request.PropertyForRentCreateRequest;
import com.koi151.msproperties.model.request.PropertyForSaleCreateRequest;
import com.koi151.msproperties.model.request.RoomCreateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper {

    PropertyMapper INSTANCE = Mappers.getMapper( PropertyMapper.class );

    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "propertyForRent", ignore = true)
    @Mapping(target = "propertyForSale", ignore = true)
    PropertyEntity toPropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "propertyEntity", ignore = true)
    PropertyForRentEntity toPropertyForRentEntity(PropertyForRentCreateRequest request);

    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "propertyEntity", ignore = true)
    PropertyForSaleEntity toPropertyForSaleEntity(PropertyForSaleCreateRequest request);

    @Mapping(target = "propertyEntity", ignore = true)
    RoomEntity toRoomEntity(RoomCreateRequest request);

    @Mapping(target = "imageUrls", ignore = true)
    @Mapping(target = "address", ignore = true)
    FullPropertyDTO toFullPropertyDTO(PropertyEntity entity);
}


