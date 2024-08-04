package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.model.projection.PropertySearchProjection;
import com.koi151.msproperties.model.projection.RoomSearchProjection;
import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import com.koi151.msproperties.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.utils.ListUtil;
import com.koi151.msproperties.utils.NumberUtil;
import com.koi151.msproperties.utils.StringUtil;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {StringUtil.class, ListUtil.class, NumberUtil.class, StatusEnum.class})
public interface PropertyMapper {

    // included rooms, propertyForRent, propertyForSale, propertyPostService, address entities
    @Mapping(target = "status", expression = ("java(StatusEnum.DRAFT)"))
    Property toPropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "view", expression = "java(NumberUtil.generateRandomInteger(0, 20000))")
    Property toFakePropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "property", ignore = true)
    PropertyForRent toPropertyForRentEntity(PropertyForRentCreateRequest request);

    @Mapping(target = "propertyId", ignore = true)
    @Mapping(target = "property", ignore = true)
    PropertyForSale toPropertyForSaleEntity(PropertyForSaleCreateRequest request);

    @Mapping(target = "property", ignore = true)
    Rooms toRoomEntity(RoomCreateUpdateRequest request);

    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName")
    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
    @Mapping(target = "propertyForRent.paymentSchedule", source = "propertyForRent.paymentSchedule.scheduleName")
    @Mapping(target = "address", expression = "java(getFullAddressStringFromEntity(entity.getAddress()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtil.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "propertyPostService.daysPosted", source = "propertyPostService.daysPosted.day")
    @Mapping(target = "propertyPostService.postingPackage", source = "propertyPostService.postingPackage.packageName")
    DetailedPropertyDTO toDetailedPropertyDTO(Property entity);

//    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName")
//    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
//    @Mapping(target = "type", expression = "java(getPropertyType(entity))")
//    @Mapping(target = "rentalPrice", source = "propertyForRent.rentalPrice")
//    @Mapping(target = "salePrice", source = "propertyForSale.salePrice")
//    @Mapping(target = "status", source = "status.statusName")
//    @Mapping(target = "rooms", source = "rooms", qualifiedByName = "mapRoomsToRoomNameQuantityDTOs")
//    @Mapping(target = "address", expression = "java(getFullAddressString(entity.getAddress()))")
//    @Mapping(target = "imageUrls", expression = "java(ListUtil.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
//    PropertySearchDTO toPropertySearchDTO(Property entity);

    @Mapping(target = "type", expression = "java(getPropertyType(projection))")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "address", expression = "java(getFullAddressString(projection.getAddress()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtil.splitStringByRegexToList(projection.getImageUrls(), \",\"))")
    @Mapping(target = "rooms", expression = "java(toRoomNameQuantityDTOs(projection.getRooms()))")
    @Mapping(target = "propertyPostService.postingPackage", source = "propertyPostService.postingPackage.packageName")
    PropertySearchDTO toPropertySearchDTO(PropertySearchProjection projection);

    // Helper function to map rooms
    default List<RoomNameQuantityDTO> toRoomNameQuantityDTOs(List<RoomSearchProjection> rooms) {
        return (rooms == null) ? null
            : rooms.stream()
                .map(room -> RoomNameQuantityDTO.builder()
                    .roomType(room.roomType().getName())
                    .quantity(room.quantity())
                    .build())
                .collect(Collectors.toList());
    }



    @Mapping(target = "status", source = "status.statusName")
    PropertiesHomeDTO toPropertiesHomeDTO(Property entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    // fields in the target object will retain their existing values if the corresponding fields in the source object are null
    @Mapping(target = "propertyForRent.rentalPrice", // get price existed in db when request price = 0.0, due to double type not accept null value
            expression = "java(propertyForRentUpdateRequest.rentalPrice() != null " +
                    "? propertyForRentUpdateRequest.rentalPrice() " +
                    ": mappingTarget.getRentalPrice())")
    @Mapping(target = "propertyForSale.salePrice",
            expression = "java(propertyForSaleUpdateRequest.salePrice() != null " +
                    "? propertyForSaleUpdateRequest.salePrice() " +
                    ": mappingTarget.getSalePrice())")
    @Mapping(target = "rooms", ignore = true)
    void updatePropertyFromDto(PropertyUpdateRequest request, @MappingTarget Property entity);

    default String getFullAddressString(Address entity) {
        List<String> addressList = Stream.of(entity.getStreetAddress(), entity.getWard(), entity.getDistrict(), entity.getCity())
                .filter(Objects::nonNull)  // Filter out null values
                .toList();  // Java 16+ or .collect(Collectors.toList())
        return StringUtil.toStringSeparateByRegex(addressList, ", ");
    }

    default String getFullAddressStringFromEntity(Address entity) { // temp
        List<String> addressList = Stream.of(entity.getStreetAddress(), entity.getWard(), entity.getDistrict(), entity.getCity())
                .filter(Objects::nonNull)  // Filter out null values
                .toList();  // Java 16+ or .collect(Collectors.toList())
        return StringUtil.toStringSeparateByRegex(addressList, ", ");
    }

//    default String getPropertyType(PropertySearchProjection entity) {
//        boolean forRent = entity.getPropertyForRent() != null;
//        boolean forSale = entity.getPropertyForSale() != null;
//
//        if (forRent && forSale) {
//            return "For rent and sale";
//        } else if (forRent) {
//            return "For rent";
//        } else if (forSale) {
//            return "For sale";
//        }
//        return "Unknown";
//    }

    default String getPropertyType(PropertySearchProjection projection) {
        boolean forRent = projection.getSalePrice() != null;
        boolean forSale = projection.getRentalPrice() != null;

        if (forRent && forSale) {
            return "For rent and sale";
        } else if (forRent) {
            return "For rent";
        } else if (forSale) {
            return "For sale";
        }
        return "Unknown";
    }


//    PropertySearchDTO convertToPropertySearchDTO(PropertySearchProjection projection);

}