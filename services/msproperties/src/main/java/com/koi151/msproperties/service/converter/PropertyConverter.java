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

    public String getAddressString(AddressEntity item) {
        return String.join(", ",
                item.getStreetAddress(),
                item.getWard(),
                item.getDistrict(),
                item.getCity());
    }

    public PropertySearchDTO toPropertySearchDTO(PropertyEntity item) {
        PropertySearchDTO propertyDTO = modelMapper.map(item, PropertySearchDTO.class);

        propertyDTO.setAddress(getAddressString(item.getAddressEntity()));

        if (item.getBalconyDirection() != null) {
            propertyDTO.setBalconyDirection(item.getBalconyDirection().getDirectionName());
        }

        if (item.getHouseDirection() != null) {
            propertyDTO.setHouseDirection(item.getHouseDirection().getDirectionName());
        }

        PropertyForRentEntity rentEntity = item.getPropertyForRentEntity();
        PropertyForSaleEntity saleEntity = item.getPropertyForSaleEntity();

        if (rentEntity != null) {
            propertyDTO.setPrice(rentEntity.getRentalPrice());
            propertyDTO.setPaymentSchedule(rentEntity.getPaymentSchedule().getScheduleName());
            propertyDTO.setTerm(rentEntity.getRentTerm());
            propertyDTO.setType(PropertyTypeEnum.RENT.getPropertyType());
        } else if (saleEntity != null) {
            propertyDTO.setPrice(saleEntity.getSalePrice());
            propertyDTO.setTerm(saleEntity.getSaleTerm());
            propertyDTO.setType(PropertyTypeEnum.SALE.getPropertyType());
        }

        if (item.getRoomEntities() != null) {
            propertyDTO.setRooms(item.getRoomEntities().stream()
                    .map(room -> RoomNameQuantityDTO.builder()
                            .roomType(room.getRoomType())
                            .quantity(room.getQuantity())
                            .build())
                    .collect(Collectors.toList()));
        }

        propertyDTO.setStatus(item.getStatus().getStatusName());
        return propertyDTO;
    }

    public FullPropertyDTO toFullPropertyDTO (PropertyEntity item) {
        FullPropertyDTO propertyDTO = modelMapper.map(item, FullPropertyDTO.class);

        propertyDTO.setAddress(getAddressString(item.getAddressEntity()));

        PropertyForRentEntity rentEntity = item.getPropertyForRentEntity();
        PropertyForSaleEntity saleEntity = item.getPropertyForSaleEntity();

        if (rentEntity != null) {
            propertyDTO.setPrice(rentEntity.getRentalPrice());
            propertyDTO.setPaymentSchedule(rentEntity.getPaymentSchedule().getScheduleName());
            propertyDTO.setTerm(rentEntity.getRentTerm());
            propertyDTO.setType(PropertyTypeEnum.RENT.getPropertyType());
        } else if (saleEntity != null) {
            propertyDTO.setPrice(saleEntity.getSalePrice());
            propertyDTO.setTerm(saleEntity.getSaleTerm());
            propertyDTO.setType(PropertyTypeEnum.SALE.getPropertyType());
        }

        if (item.getRoomEntities() != null) {
            propertyDTO.setRooms(item.getRoomEntities().stream()
                    .map(room -> RoomNameQuantityDTO.builder()
                            .roomType(room.getRoomType())
                            .quantity(room.getQuantity())
                            .build())
                    .collect(Collectors.toList()));
        }

        propertyDTO.setStatus(item.getStatus().getStatusName());

        return propertyDTO;
    }

    public PropertyEntity toPropertyEntity (PropertyCreateRequest request, List<MultipartFile> imageFiles) {
        PropertyEntity propertyEntity =  modelMapper.map(request, PropertyEntity.class);

        if (imageFiles != null && !imageFiles.isEmpty()) {
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
