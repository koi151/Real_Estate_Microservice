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

    public PropertyEntity toPropertyEntity (PropertyCreateRequest request, List<MultipartFile> imageFiles, boolean generateFakeProperties) {
        PropertyEntity propertyEntity;
        if (generateFakeProperties)
            propertyEntity = propertyMapper.toFakePropertyEntity(request);
        else
            propertyEntity = propertyMapper.toPropertyEntity(request);

        propertyEntity.getPropertyPostService().setPropertyEntity(propertyEntity);
        propertyEntity.getAddress().setPropertyEntity(propertyEntity);

        if (request.getPropertyForSale() != null)
            propertyEntity.getPropertyForSale().setPropertyEntity(propertyEntity);
        if (request.getPropertyForRent() != null)
            propertyEntity.getPropertyForRent().setPropertyEntity(propertyEntity);

        if (request.getRooms() != null)
            propertyEntity.getRooms()
                    .forEach(roomEntity -> roomEntity.setPropertyEntity(propertyEntity));

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
