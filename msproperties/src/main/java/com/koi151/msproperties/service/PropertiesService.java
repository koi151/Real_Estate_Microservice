package com.koi151.msproperties.service;

import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.Properties;
import com.koi151.msproperties.entity.StatusEnum;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertiesService implements PropertiesServiceImp {

    @Autowired
    PropertiesRepository propertiesRepository;

    @Autowired
    CloudinaryService cloudinaryService;

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
            propertiesHomeDTO.setStatusEnum(propertiesHomeDTO.getStatusEnum());
            propertiesHomeDTO.setView(propertiesHomeDTO.getView());

            propertiesHomeDTOList.add((propertiesHomeDTO));
        }

        return propertiesHomeDTOList;
    }

    @Override
    public Properties createProperty(PropertyCreateRequest request) {
        Properties properties = new Properties();

        properties.setTitle(request.getTitle());
        properties.setCategoryId(request.getCategoryId());
        properties.setArea(request.getArea());
        properties.setDescription(request.getDescription());

        properties.setTotalFloor(request.getTotalFloor());
        properties.setHouseDirectionEnum(request.getHouseDirectionEnum());
        properties.setBalconyDirectionEnum(request.getBalconyDirectionEnum());
        properties.setAvailableFrom(request.getAvailableFrom());

        properties.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        if(request.getImages() != null && !request.getImages().isEmpty()) {
            String imageUrls = cloudinaryService.uploadFile(request.getImages(), "real_estate_properties");                if (imageUrls == null || imageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload image to Cloudinary");
            }
            properties.setImageUrls(imageUrls);
        }

        propertiesRepository.save(properties);

        return properties;
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
            propertiesHomeDTO.setStatusEnum(property.getStatusEnum());
            propertiesHomeDTO.setImages(property.getImageUrls());
            propertiesHomeDTO.setView(property.getView());

            propertiesHomeDTOList.add(propertiesHomeDTO);
        }

        return propertiesHomeDTOList;
    }

//    @Override
//    public List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status) {
//        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
//        Page<Properties> properties = propertiesRepository.findByStatusEnum(status, pageRequest);
//
//        List<PropertiesHomeDTO> propertiesHomeDTOList = new ArrayList<>();
//        for (Properties property: properties) {
//            PropertiesHomeDTO propertiesHomeDTO = new PropertiesHomeDTO();
//
//            propertiesHomeDTO.setDescription(property.getDescription());
//            propertiesHomeDTO.setStatusEnum(property.getStatusEnum());
//            propertiesHomeDTO.setView(property.getView());
//            propertiesHomeDTO.setImages(property.getImageUrls());
//
//            propertiesHomeDTOList.add(propertiesHomeDTO);
//        }
//
//        return propertiesHomeDTOList;
//    }

    @Override
    public List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findByStatusEnum(status, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatusEnum(), property.getView()))
                .collect(Collectors.toList());
    }


    @Override
    public Properties updateProperty(Integer id, PropertyUpdateRequest request) throws PropertyNotFoundException {
        return propertiesRepository.findById(id)
                .map(existingProperty -> {

                    if (request.getTitle() != null)
                        existingProperty.setTitle(request.getTitle());
                    if (request.getDescription() != null)
                        existingProperty.setDescription(request.getDescription());
                    if (request.getCategoryId() != null)
                        existingProperty.setCategoryId(request.getCategoryId());

                    if (request.getArea() != null)
                        existingProperty.setArea(request.getArea());
                    if (request.getHouseDirectionEnum() != null)
                        existingProperty.setHouseDirectionEnum(request.getHouseDirectionEnum());
                    if (request.getBalconyDirectionEnum() != null)
                        existingProperty.setBalconyDirectionEnum(request.getBalconyDirectionEnum());

                    if (request.getAvailableFrom() != null)
                        existingProperty.setAvailableFrom(request.getAvailableFrom());
                    if(request.getStatusEnum() != null)
                        existingProperty.setStatusEnum(request.getStatusEnum());
                    if (request.getPrice() != null) {
                        existingProperty.setPrice(request.getPrice());
                    }

                    if (request.getImages() != null) {
                        String imageUrls = cloudinaryService.uploadFile(request.getImages(), "real_estate_properties");
                        if (imageUrls == null || imageUrls.isEmpty()) {
                            throw new RuntimeException("Failed to upload image to Cloudinary");
                        }
                        existingProperty.setImageUrls(imageUrls);
                    }

                    existingProperty.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                    return propertiesRepository.save(existingProperty);

                })
                .map(Properties::new)
                .orElseThrow(() -> new PropertyNotFoundException("Cannot found property with id: " + id));

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
