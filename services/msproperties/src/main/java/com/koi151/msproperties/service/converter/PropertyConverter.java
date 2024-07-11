package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.mapper.PropertyMapper;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import com.koi151.msproperties.service.impl.CloudinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PropertyConverter {

//    private final ModelMapper modelMapper;
    private final PropertyMapper propertyMapper;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

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
