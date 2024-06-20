package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.model.request.AddressRequest;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import com.koi151.msproperties.model.request.RoomCreateRequest;
import com.koi151.msproperties.service.impl.CloudinaryServiceImpl;
import com.koi151.msproperties.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PropertyConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    public PropertySearchDTO toPropertySearchDTO(PropertyEntity item) {
        PropertySearchDTO property = modelMapper.map(item, PropertySearchDTO.class);

        String address = String.join(", ",
                item.getAddressEntity().getStreetAddress(),
                item.getAddressEntity().getWard(),
                item.getAddressEntity().getDistrict(),
                item.getAddressEntity().getCity());
        property.setAddress(address);

        if (item.getBalconyDirection() != null) {
            property.setBalconyDirection(item.getBalconyDirection().getDirectionName());
        }

        if (item.getHouseDirection() != null) {
            property.setHouseDirection(item.getHouseDirection().getDirectionName());
        }

        PropertyForRentEntity rentEntity = item.getPropertyForRentEntity();
        PropertyForSaleEntity saleEntity = item.getPropertyForSaleEntity();

        if (rentEntity != null) {
            property.setPrice(rentEntity.getRentalPrice());
            property.setPaymentSchedule(rentEntity.getPaymentSchedule().getScheduleName());
            property.setTerm(rentEntity.getRentTerm());
            property.setType(PropertyTypeEnum.RENT.getPropertyType());
        } else if (saleEntity != null) {
            property.setPrice(saleEntity.getSalePrice());
            property.setTerm(saleEntity.getSaleTerm());
            property.setType(PropertyTypeEnum.SALE.getPropertyType());
        }

        if (item.getRoomEntities() != null) {
            property.setRooms(item.getRoomEntities().stream()
                    .map(room -> RoomNameQuantityDTO.builder()
                            .roomType(room.getRoomType())
                            .quantity(room.getQuantity())
                            .build())
                    .collect(Collectors.toList()));
        }

        property.setStatus(item.getStatus().getStatusName());
        return property;
    }

    public FullPropertyDTO toFullPropertyDTO (PropertyEntity item) {
        FullPropertyDTO property = modelMapper.map(item, FullPropertyDTO.class);

        return property;
    }

    public PropertyEntity toPropertyEntity (PropertyCreateRequest request, List<MultipartFile> imageFiles) {
        PropertyEntity propertyEntity =  modelMapper.map(request, PropertyEntity.class);

        if (imageFiles != null) {
            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_properties");
            if (imageUrls == null || imageUrls.isEmpty())
                throw new RuntimeException("Failed to upload images to Cloudinary");

            propertyEntity.setImageUrls(imageUrls);
        }

        if (request.getType().isPropertyForRent()) {
            propertyEntity.setPropertyForRentEntity(PropertyForRentEntity.builder()
                    .propertyEntity(propertyEntity)
                    .rentalPrice(request.getPrice())
                    .rentTerm(request.getTerm())
                    .paymentSchedule(request.getPaymentSchedule())
                    .build());
        } else if (request.getType().isPropertyForSale()) {
            propertyEntity.setPropertyForSaleEntity(PropertyForSaleEntity.builder()
                    .propertyEntity(propertyEntity)
                    .salePrice(request.getPrice())
                    .saleTerm(request.getTerm())
                    .build());
        }

        if (request.getRooms() != null && !request.getRooms().isEmpty()) {
            Set<RoomEntity> roomEntities = request.getRooms().stream()
                    .map(roomRequest -> RoomEntity.builder()
                            .propertyEntity(propertyEntity)
                            .roomType(roomRequest.getRoomType())
                            .quantity(roomRequest.getQuantity())
                            .build())
                    .collect(Collectors.toSet());
            propertyEntity.setRoomEntities(roomEntities);
        }

         return propertyEntity;
    }

    public AddressEntity toAddressEntity(AddressRequest request) {
        return modelMapper.map(request, AddressEntity.class);
    }

}
