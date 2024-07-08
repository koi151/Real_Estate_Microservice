package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.mapper.PropertyMapper;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import com.koi151.msproperties.service.impl.CloudinaryServiceImpl;
import com.koi151.msproperties.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PropertyConverter {

    private final ModelMapper modelMapper;
    private final PropertyMapper propertyMapper;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public String getFullAddressString(AddressEntity item) {
        StringBuilder fullAddress = new StringBuilder();
        if (StringUtil.checkString(item.getStreetAddress()))
            fullAddress.append(item.getStreetAddress()).append(", ");
        if (StringUtil.checkString(item.getWard()))
            fullAddress.append(item.getWard()).append(", ");
        if (StringUtil.checkString(item.getDistrict()))
            fullAddress.append(item.getDistrict()).append(", ");
        if (StringUtil.checkString(item.getCity()))
            fullAddress.append(item.getCity()).append(", ");

        return fullAddress.substring(0, fullAddress.length() - 2);
    }

    public PropertySearchDTO toPropertySearchDTO(PropertyEntity item) {
        PropertySearchDTO propertyDTO = modelMapper.map(item, PropertySearchDTO.class);

//        propertyDTO.setAddress(getAddressString(item.getAddress())); // temp

        if (item.getBalconyDirection() != null) {
            propertyDTO.setBalconyDirection(item.getBalconyDirection().getDirectionName());
        }

        if (item.getHouseDirection() != null) {
            propertyDTO.setHouseDirection(item.getHouseDirection().getDirectionName());
        }

//        PropertyForRentEntity rentEntity = item.getPropertyForRent();
//        PropertyForSaleEntity saleEntity = item.getPropertyForSale();

//        if (rentEntity != null) {
//            propertyDTO.setPrice(rentEntity.getRentalPrice());
//            propertyDTO.setPaymentSchedule(rentEntity.getPaymentSchedule().getScheduleName());
//            propertyDTO.setTerm(rentEntity.getRentTerm());
//            propertyDTO.setType(PropertyTypeEnum.RENT.getPropertyType());
//        } else if (saleEntity != null) {
//            propertyDTO.setPrice(saleEntity.getSalePrice());
//            propertyDTO.setTerm(saleEntity.getSaleTerm());
//            propertyDTO.setType(PropertyTypeEnum.SALE.getPropertyType());
//        }

//        if (item.getRooms() != null) { // temp
//            propertyDTO.setRooms(item.getRooms().stream()
//                    .map(room -> RoomNameQuantityDTO.builder()
//                            .roomType(room.getRoomType())
//                            .quantity(room.getQuantity())
//                            .build())
//                    .collect(Collectors.toList()));
//        }

        propertyDTO.setStatus(item.getStatus().getStatusName());
        return propertyDTO;
    }

    public FullPropertyDTO toFullPropertyDTO (PropertyEntity entity) {
        FullPropertyDTO propertyDTO = propertyMapper.toFullPropertyDTO(entity);
        propertyDTO.setAddress(getFullAddressString(entity.getAddress()));

        if (entity.getImageUrls() != null && !entity.getImageUrls().isEmpty()) {
            List<String> imageUrls = Arrays.stream(entity.getImageUrls().split(",")).toList();
            propertyDTO.setImageUrls(imageUrls);
        }

        propertyDTO.setStatus(entity.getStatus().getStatusName());
        return propertyDTO;
    }

    public PropertyEntity toPropertyEntity (PropertyCreateRequest request, List<MultipartFile> imageFiles, AddressEntity addressEntity) {
        PropertyEntity propertyEntity = propertyMapper.toPropertyEntity(request);
        propertyEntity.setAddress(addressEntity);

        // Handle PropertyForRentEntity if it exists
        if (request.getPropertyForRent() != null) {
            PropertyForRentEntity propertyForRentEntity = propertyMapper.toPropertyForRentEntity(request.getPropertyForRent());
            propertyForRentEntity.setPropertyEntity(propertyEntity); // Associate with propertyEntity
            propertyEntity.setPropertyForRent(propertyForRentEntity);
        }

        // Handle PropertyForSaleEntity if it exists
        if (request.getPropertyForSale() != null) {
            PropertyForSaleEntity propertyForSaleEntity = propertyMapper.toPropertyForSaleEntity(request.getPropertyForSale());
            propertyForSaleEntity.setPropertyEntity(propertyEntity);
            propertyEntity.setPropertyForSale(propertyForSaleEntity);
        }

        // Handle RoomEntity if it exists
        if (request.getRooms() != null && !request.getRooms().isEmpty()) {
            List<RoomEntity> roomEntities = request.getRooms().stream()
                    .map(roomRequest -> {
                        RoomEntity roomEntity = propertyMapper.toRoomEntity(roomRequest);
                        roomEntity.setPropertyEntity(propertyEntity);
                        return roomEntity;
                    })
                    .collect(Collectors.toList());

            propertyEntity.setRooms(roomEntities);
        }

        // add image to cloudinary and save to db as url strings
        if (imageFiles != null && !imageFiles.isEmpty()) {
            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_properties");
            if (imageUrls == null || imageUrls.isEmpty())
                throw new RuntimeException("Failed to upload images to Cloudinary");

            propertyEntity.setImageUrls(imageUrls);
        }

        return propertyEntity;
    }
}
