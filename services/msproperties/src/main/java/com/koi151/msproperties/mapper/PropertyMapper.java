package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.model.dto.FullPropertyDTO;
import com.koi151.msproperties.model.request.*;
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
    RoomEntity toRoomEntity(RoomCreateUpdateRequest request);

    @Mapping(target = "imageUrls", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName") // Mapping directionName instead of enum
    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
    @Mapping(target = "propertyForRent.paymentSchedule", source = "propertyForRent.paymentSchedule.scheduleName")
    FullPropertyDTO toFullPropertyDTO(PropertyEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS) // fields in the target object will retain their existing values if the corresponding fields in the source object are null
    @Mapping(target = "propertyForRent.rentalPrice", // get price existed in db when request price = 0.0, due to double type not accept null value
            expression = "java(propertyForRentUpdateRequest.getRentalPrice() != 0.0 " +
                    "? propertyForRentUpdateRequest.getRentalPrice() " +
                    ": mappingTarget.getRentalPrice())")
    @Mapping(target = "propertyForSale.salePrice",
            expression = "java(propertyForSaleUpdateRequest.getSalePrice() != 0.0 " +
                    "? propertyForSaleUpdateRequest.getSalePrice() " +
                    ": mappingTarget.getSalePrice())")
    @Mapping(target = "rooms", ignore = true)
    void updatePropertyFromDto(PropertyUpdateRequest request, @MappingTarget PropertyEntity entity);
}


