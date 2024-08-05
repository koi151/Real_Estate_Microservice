package com.koi151.msproperties.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.RoomTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.mapper.PropertyMapper;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.model.projection.PropertySearchProjection;
import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.repository.*;
import com.koi151.msproperties.repository.custom.impl.PropertyRepositoryImpl;
import com.koi151.msproperties.service.PropertiesService;
import com.koi151.msproperties.customExceptions.MaxImagesExceededException;
import com.koi151.msproperties.customExceptions.PropertyNotFoundException;
import com.koi151.msproperties.service.converter.PropertyConverter;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertiesService {

    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyConverter propertyConverter;
    private final PropertyMapper propertyMapper;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(PropertyRepositoryImpl.class);

    public Page<PropertySearchDTO> findAllProperties(PropertySearchRequest request, Pageable pageable) {

        String redisKey = "properties:" + request.toString() + ":" + pageable.getPageNumber() + ":" + pageable.getPageSize();
        String redisData = redisTemplate.opsForValue().get(redisKey);

        try {
            if (redisData == null) {
                Page<PropertySearchProjection> propertyPage = propertyRepository.findPropertiesByCriteria(request, pageable);
                Page<PropertySearchDTO> result = propertyPage.map(propertyMapper::toPropertySearchDTO);
                String jsonData = objectMapper.writeValueAsString(result);
                redisTemplate.opsForValue().set(redisKey, jsonData);
                return result;
            } else {
                JavaType pageType = objectMapper.getTypeFactory().constructParametricType(Page.class, PropertySearchDTO.class);
                return objectMapper.readValue(redisData, pageType);
            }
        } catch (JsonProcessingException ex) {
            logger.error("Json processing error occurred "  + ex.getMessage());
            throw new RuntimeException("Json processing error occurred");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByDeleted(false, pageable);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailedPropertyDTO getPropertyById(Long id) {
        Property property = findPropertyExistedById(id);
        return propertyMapper.toDetailedPropertyDTO(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByCategoryIdAndDeleted(categoryId, false, pageable);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertySearchDTO> findAllPropertiesByAccount(Long accountId, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByAccountIdAndDeleted(accountId, false, pageable);
//        return properties.map(propertyMapper::toPropertySearchDTO);
        return null; // temp
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertiesHomeDTO> findPropertiesByStatus(StatusEnum status, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByStatus(status, pageable);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Transactional
    @Override
    public DetailedPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> imageFiles) {
        Property property = propertyConverter.toPropertyEntity(request, imageFiles, false);
        propertyRepository.save(property);
        return propertyMapper.toDetailedPropertyDTO(property);
    }

    @Transactional
    public void createFakeProperties(List<PropertyCreateRequest> fakeProperties) {

        // Create property entities in parallel
        List<Property> propertyEntities = fakeProperties.parallelStream()
                .map(fakeProperty -> propertyConverter.toPropertyEntity(fakeProperty,null, true))
                .toList();

        // Bulk save all property entities
        propertyRepository.saveAllAndFlush(propertyEntities);
    }

    @Transactional
    @Override
    public DetailedPropertyDTO updateProperty(Long id, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
        return propertyRepository.findByPropertyIdAndDeleted(id, false)
                .map(existingProperty -> {
                    updatePropertyDetails(existingProperty, request);
                    updateImages(existingProperty, request, imageFiles);

                    return propertyRepository.save(existingProperty);
                })
                .map(propertyMapper::toDetailedPropertyDTO)
                .orElseThrow(() -> new PropertyNotFoundException("Cannot find account with id: " + id));

    }

    @Transactional(readOnly = true)
    public Property findPropertyExistedById(Long id) {
        return propertyRepository.findByPropertyIdAndDeleted(id, false)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + id));
    }

    @Transactional(readOnly = true)
    public boolean checkPropertyExistedById(Long id) {
        return propertyRepository.existsByPropertyIdAndDeleted(id, false);
    }

    private void updateImages(Property existingProperty, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
        Set<String> existingImagesUrlSet = new HashSet<>();

        // Initialize existingImagesUrlSet with current image URLs if they exist
        if (existingProperty.getImageUrls() != null && !existingProperty.getImageUrls().isEmpty()) {
            existingImagesUrlSet = new HashSet<>(Arrays.asList(existingProperty.getImageUrls().split(",")));
        }

        // Handle image removal
        if (request != null && request.imageUrlsRemove() != null && !request.imageUrlsRemove().isEmpty()) {
            request.imageUrlsRemove().forEach(existingImagesUrlSet::remove); // faster than using removeALl
        }

        // Handle image addition
        if (imageFiles != null && !imageFiles.isEmpty()) {
            int totalImagesAfterAddition = existingImagesUrlSet.size() + imageFiles.size();
            if (totalImagesAfterAddition > 8) {
                throw new MaxImagesExceededException("Cannot store more than 8 images. You are trying to add " + imageFiles.size() + " images to the existing " + existingImagesUrlSet.size() + " images.");
            }

            String newImageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_categories");
            if (newImageUrls == null || newImageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload images to Cloudinary");
            }
            existingImagesUrlSet.addAll(Arrays.asList(newImageUrls.split(",")));
        }

        // Convert updated set of image URLs back to a comma-separated string
        String updatedImageUrls = String.join(",", existingImagesUrlSet);
        existingProperty.setImageUrls(updatedImageUrls.isEmpty() ? null : updatedImageUrls);
    }

    @Transactional
    protected void updatePropertyDetails(Property existingProperty, PropertyUpdateRequest request) {
        if (request != null) {
            updateExistingPropertyRooms(existingProperty, request.rooms()); // update rooms info separately
            propertyMapper.updatePropertyFromDto(request, existingProperty);
        }
    }

    private void updateExistingPropertyRooms(Property existingProperty, List<RoomCreateUpdateRequest> updatedRooms) {
        List<Rooms> currentRooms = existingProperty.getRooms();

        for (RoomCreateUpdateRequest updatedRoom : updatedRooms) {
            RoomTypeEnum roomType = updatedRoom.roomType();
            short quantity = updatedRoom.quantity();

            Rooms existingRooms = currentRooms.stream()
                    .filter(room -> room.getRoomType().equals(roomType))
                    .findFirst()
                    .orElse(null);

            if (existingRooms != null) {
                // Room type exists in the property
                if (existingRooms.getQuantity() != quantity) // Quantity is different, update it
                    existingRooms.setQuantity(quantity);
                // if quantity is the same, do nothing
            } else {
                // Room type doesn't exist, create a new room
                Rooms newRooms = Rooms.builder()
                        .roomType(roomType)
                        .quantity(quantity)
                        .property(existingProperty)
                        .build();

                roomRepository.save(newRooms);
                currentRooms.add(newRooms);
            }
        }
        existingProperty.setRooms(currentRooms);
    }

    @Override
    @Transactional
    public void deleteProperty(Long id) throws PropertyNotFoundException {
        propertyRepository.findById(Math.toIntExact(id))
                .map(existingProperty -> {
                    existingProperty.setDeleted(true);
                    return propertyRepository.save(existingProperty);
                })
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
    }

    @Override
    @Transactional
    public boolean propertyExistsCheck(Long id) {
        return checkPropertyExistedById(id);
    }
}
