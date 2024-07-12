package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.model.dto.DetailedPropertyDTO;
import com.koi151.msproperties.model.dto.PropertiesHomeDTO;
import com.koi151.msproperties.model.dto.PropertySearchDTO;
import com.koi151.msproperties.model.request.*;
import com.koi151.msproperties.utils.ListUtil;
import com.koi151.msproperties.utils.StringUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {StringUtil.class, ListUtil.class})
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

    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName") // Mapping directionName instead of enum
    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
    @Mapping(target = "propertyForRent.paymentSchedule", source = "propertyForRent.paymentSchedule.scheduleName")
    @Mapping(target = "address", expression = "java(getFullAddressString(entity.getAddress()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtil.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
    @Mapping(target = "status", source = "status.statusName")
    DetailedPropertyDTO toDetailedPropertyDTO(PropertyEntity entity);

    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName") // Mapping directionName instead of enum
    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
    @Mapping(target = "type", expression = "java(getPropertyType(entity))")
    @Mapping(target = "rentalPrice", source = "propertyForRent.rentalPrice")
    @Mapping(target = "salePrice", source = "propertyForSale.salePrice")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "address", expression = "java(getFullAddressString(entity.getAddress()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtil.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
    PropertySearchDTO toPropertySearchDTO(PropertyEntity entity);

    @Mapping(target = "status", source = "status.statusName")
    PropertiesHomeDTO toPropertiesHomeDTO(PropertyEntity entity);

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

    default String getFullAddressString(AddressEntity entity) {
        List<String> addressList = Stream.of(entity.getStreetAddress(), entity.getWard(), entity.getDistrict(), entity.getCity())
                .filter(Objects::nonNull)  // Filter out null values
                .toList();  // Java 16+ or .collect(Collectors.toList())
        return StringUtil.toStringSeparateByRegex(addressList, ", ");
    }

    default String getPropertyType(PropertyEntity entity) {
        boolean forRent = entity.getPropertyForRent() != null;
        boolean forSale = entity.getPropertyForSale() != null;

        if (forRent && forSale) {
            return "For rent and sale";
        } else if (forRent) {
            return "For rent";
        } else if (forSale) {
            return "For sale";
        }
        return "Unknown";
    }
}