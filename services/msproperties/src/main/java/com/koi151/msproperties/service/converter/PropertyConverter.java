package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.mapper.PropertyMapper;
import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import com.koi151.msproperties.service.impl.CloudinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyConverter {

//    private final ModelMapper modelMapper;
    private final PropertyMapper propertyMapper;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public Property toPropertyEntity (PropertyCreateRequest request, List<MultipartFile> imageFiles, boolean generateFakeProperties) {
        Property property;
        if (generateFakeProperties)
            property = propertyMapper.toFakePropertyEntity(request);
        else
            property = propertyMapper.toPropertyEntity(request);

        property.getAddress().setProperty(property);

        if (request.propertyForSale() != null)
            property.getPropertyForSale().setProperty(property);
        if (request.propertyForRent() != null)
            property.getPropertyForRent().setProperty(property);

        if (request.rooms() != null)
            property.getRooms()
                    .forEach(roomEntity -> roomEntity.setProperty(property));

        // add image to cloudinary and save to db as url strings
        if (imageFiles != null && !imageFiles.isEmpty()) {
            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_properties");
            if (imageUrls == null || imageUrls.isEmpty())
                throw new RuntimeException("Failed to upload images to Cloudinary");

            property.setImageUrls(imageUrls);
        }

        return property;
    }
}
