package com.koi151.msproperties.service;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.payload.request.PropertyRequest;
import com.koi151.msproperties.repository.PropertiesRepository;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import customExceptions.PropertyNotFoundException;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertiesService implements PropertiesServiceImp {

    @Autowired
    PropertiesRepository propertiesRepository;

    @Override
    public List<PropertiesHomeDTO> getHomeProperties() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findByDeleted(false, pageRequest);

        List<PropertiesHomeDTO> propertiesHomeDTOList = new ArrayList<>();

        for (Properties property: properties) {
            PropertiesHomeDTO propertiesHomeDTO = new PropertiesHomeDTO();
            propertiesHomeDTO.setTitle(property.getTitle());
            propertiesHomeDTO.setDescription(property.getDescription());
            propertiesHomeDTO.setImages(propertiesHomeDTO.getImages());
            propertiesHomeDTO.setStatus(propertiesHomeDTO.getStatus());
            propertiesHomeDTO.setView(propertiesHomeDTO.getView());

            propertiesHomeDTOList.add((propertiesHomeDTO));
        }

        return propertiesHomeDTOList;
    }

    @Override
    public boolean createProperty(PropertyRequest propertyRequest) {
        boolean isCreateSuccess = false;

        try {
            Properties properties = new Properties();

            properties.setTitle(propertyRequest.getTitle());
            properties.setCategoryId(propertyRequest.getCategoryId());
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

    @Override
    public List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findPropertiesByCategoryId(categoryId, pageRequest);

        List<PropertiesHomeDTO> propertiesHomeDTOList = new ArrayList<>();

        for (Properties property: properties) {
            PropertiesHomeDTO propertiesHomeDTO = new PropertiesHomeDTO();

            propertiesHomeDTO.setTitle(property.getTitle());
            propertiesHomeDTO.setDescription(property.getDescription());
            propertiesHomeDTO.setStatus(property.getStatus());
            propertiesHomeDTO.setImages(property.getImages());
            propertiesHomeDTO.setView(property.getView());

            propertiesHomeDTOList.add(propertiesHomeDTO);
        }

        return propertiesHomeDTOList;
    }

    @Override
    public void deleteProperty(Integer id) throws PropertyNotFoundException {
        propertiesRepository.findById(id)
                .map(existingProperty -> {
                    existingProperty.setDeleted(true);
                    return propertiesRepository.save(existingProperty);
                })
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
    }
}
