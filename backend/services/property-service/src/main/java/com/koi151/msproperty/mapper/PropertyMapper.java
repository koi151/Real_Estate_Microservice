package com.koi151.msproperty.mapper;

import com.koi151.msproperty.entity.*;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.model.dto.*;
import com.koi151.msproperty.model.projection.PropertySearchProjection;
import com.koi151.msproperty.model.projection.RoomSearchProjection;
import com.koi151.msproperty.model.request.property.PropertyCreateRequest;
import com.koi151.msproperty.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.utils.ListUtils;
import com.koi151.msproperty.utils.NumberUtils;
import com.koi151.msproperty.utils.StringUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {StringUtils.class, ListUtils.class, NumberUtils.class, StatusEnum.class})
public interface PropertyMapper {

    // included rooms, propertyForRent, propertyForSale, propertyPostService, address entities
    @Mapping(target = "status", expression = ("java(StatusEnum.PENDING)"))
    Property toPropertyEntity(PropertyCreateRequest request);

    @Mapping(target = "view", expression = "java(NumberUtils.generateRandomInteger(0, 20000))")
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
    @Mapping(target = "imageUrls", expression = "java(ListUtils.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "legalDocument", source = "legalDocument.legalDocumentName")
    @Mapping(target = "furniture", source = "furniture.furnitureName")
    @Mapping(target = "rooms", expression = "java(toRoomNameQuantityDTOs(entity.getRooms()))")
    DetailedPropertyDTO toDetailedPropertyDTO(Property entity);

    @Mapping(target = "balconyDirection", source = "balconyDirection.directionName")
    @Mapping(target = "houseDirection", source = "houseDirection.directionName")
    @Mapping(target = "type", expression = "java(getPropertyTypeFromEntity(entity))")
    @Mapping(target = "rentalPrice", source = "propertyForRent.rentalPrice")
    @Mapping(target = "salePrice", source = "propertyForSale.salePrice")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "legalDocument", source = "legalDocument.legalDocumentName")
    @Mapping(target = "furniture", source = "furniture.furnitureName")
    @Mapping(target = "address", expression = "java(getFullAddressString(entity.getAddress()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtils.splitStringByRegexToList(entity.getImageUrls(), \",\"))")
    PropertySearchDTO toPropertySearchDTO(Property entity);

    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "legalDocument", source = "legalDocument.legalDocumentName")
    @Mapping(target = "furniture", source = "furniture.furnitureName")
    @Mapping(target = "rentalPrice", source = "propertyForRent.rentalPrice")
    @Mapping(target = "salePrice", source = "propertyForSale.salePrice")
    @Mapping(target = "type", expression = "java(getPropertyTypeFromProjection(projection))")
    @Mapping(target = "address", expression = "java(getFullAddressString(projection.address()))")
    @Mapping(target = "imageUrls", expression = "java(ListUtils.splitStringByRegexToList(projection.imageUrls(), \",\"))")
    @Mapping(target = "rooms", expression = "java(projectionToRoomNameQuantityDTOs(projection.rooms()))")
    PropertySearchDTO toPropertySearchDTO(PropertySearchProjection projection);


    default List<RoomNameQuantityDTO> projectionToRoomNameQuantityDTOs(List<RoomSearchProjection> rooms) {
        return (rooms == null || rooms.isEmpty()) ? null
            : rooms.stream()
            .filter(room -> room != null && room.roomType() != null && room.quantity() != null)
            .map(room -> RoomNameQuantityDTO.builder()
                .roomType(room.roomType())
                .quantity(room.quantity())
                .build())
            .collect(Collectors.toList());
    }


    default List<RoomNameQuantityDTO> toRoomNameQuantityDTOs(List<Rooms> rooms) {
        return (rooms == null) ? null
            : rooms.stream()
            .map(room -> RoomNameQuantityDTO.builder()
                .roomType(room.getRoomType().getName())
                .quantity(room.getQuantity())
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
        List<String> addressList = Stream.of(entity.getCity(), entity.getDistrict(), entity.getWard(), entity.getStreetAddress())
                .filter(Objects::nonNull)  // Filter out null values
                .toList();
        return StringUtils.toStringSeparateByRegex(addressList, ", ");
    }

    default String getFullAddressStringFromEntity(Address entity) { // temp
        List<String> addressList = Stream.of(entity.getStreetAddress(), entity.getWard(), entity.getDistrict(), entity.getCity())
                .filter(Objects::nonNull)  // Filter out null values
                .toList();  // Java 16+ or .collect(Collectors.toList())
        return StringUtils.toStringSeparateByRegex(addressList, ", ");
    }

    default String getPropertyTypeFromEntity(Property entity) {
        boolean forRent = entity.getPropertyForRent() != null;
        boolean forSale = entity.getPropertyForSale() != null;

        if (forRent) {
            return "For rent";
        } else if (forSale) {
            return "For sale";
        }
        return "Unknown";
    }

    default String getPropertyTypeFromProjection(PropertySearchProjection projection) {
        boolean forRent = projection.propertyForRent() != null;
        boolean forSale = projection.propertyForSale() != null;
         if (forRent) {
            return "For rent";
        } else if (forSale) {
            return "For sale";
        }
        return "Unknown";
    }


//    PropertySearchDTO convertToPropertySearchDTO(PropertySearchProjection projection);

}