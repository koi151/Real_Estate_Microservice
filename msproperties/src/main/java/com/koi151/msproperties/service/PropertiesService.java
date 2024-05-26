package com.koi151.msproperties.service;

import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.payload.request.PropertyRequest;
import com.koi151.msproperties.repository.PropertiesRepository;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PropertiesService implements PropertiesServiceImp {

    @Autowired
    PropertiesRepository propertiesRepository;

    @Override
    public boolean createProperty(PropertyRequest propertyRequest) {
        boolean isCreateSuccess = false;

        try {
            Properties properties = new Properties();

            properties.setTitle(propertyRequest.getTitle());
            properties.setArea(propertyRequest.getArea());
            properties.setDescription(propertyRequest.getDescription());
            properties.setImages(propertyRequest.getImages());
            properties.setTotalFloor(propertyRequest.getTotalFloor());
            properties.setHouseDirection(propertyRequest.getHouseDirection());
            properties.setBalconyDirection(propertyRequest.getBalconyDirection());
            properties.setAvailabeFrom(propertyRequest.getAvailableFrom());
            properties.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            propertiesRepository.save(properties);
            isCreateSuccess = true;

        } catch (Exception e) {
            System.out.println("Error occurred while creating property: " + e.getMessage());
        }

        return isCreateSuccess;
    }
}
