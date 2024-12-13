package com.koi151.msproperty.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperty.client.PropertyClient;
import com.koi151.msproperty.entity.*;
import com.koi151.msproperty.enums.RoomTypeEnum;
import com.koi151.msproperty.enums.Status;
import com.koi151.msproperty.enums.StatusEnum;
import com.koi151.msproperty.mapper.PropertyMapper;
import com.koi151.msproperty.model.dto.*;
import com.koi151.msproperty.model.projection.PropertySearchProjection;
import com.koi151.msproperty.model.request.PropertyServicePackageCreateRequest;
import com.koi151.msproperty.model.request.property.PropertyCreateRequest;
import com.koi151.msproperty.model.request.property.PropertySearchRequest;
import com.koi151.msproperty.model.request.property.PropertyStatusUpdateRequest;
import com.koi151.msproperty.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.model.response.ResponseData;
import com.koi151.msproperty.repository.*;
import com.koi151.msproperty.repository.custom.impl.PropertyRepositoryImpl;
import com.koi151.msproperty.service.PropertiesService;
import com.koi151.msproperty.customExceptions.MaxImagesExceededException;
import com.koi151.msproperty.customExceptions.PropertyNotFoundException;
import com.koi151.msproperty.service.converter.PropertyConverter;
import com.koi151.msproperty.validator.PropertyValidator;
import com.koi151.property_submissions.customExceptions.EntityNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertiesService {

    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyConverter propertyConverter;
    private final PropertyMapper propertyMapper;
    private final PropertyValidator propertyValidator;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PropertyClient propertyClient;

    private static final String CACHE_KEY_PATTERN = "properties:%s:%d:%d";
    private static final int CACHE_TTL_SECONDS = 20;

//    public Page searchPropertiesForAdmin(PropertySearchRequest request, Pageable pageable) {
//        String redisKey = "properties:" + request.toString() + ":" + pageable.getPageNumber() + ":" + pageable.getPageSize();
//        String redisData = redisTemplate.opsForValue().get(redisKey);
//        try {
//            if (redisData == null) {
//                Page<PropertySearchProjection> pages = propertyRepository.findPropertiesForAdmin(request, pageable);
//                String jsonData = objectMapper.writeValueAsString(result);
//                redisTemplate.opsForValue().set(redisKey, jsonData, 20, TimeUnit.SECONDS);
//                return result;
//            } else {
//                JavaType pageType = objectMapper.getTypeFactory().constructParametricType(Page.class, PropertySearchDTO.class);
//                return objectMapper.readValue(redisData, pageType);
//            }
//        } catch (JsonProcessingException ex) {
//            log.error("Json processing error occurred "  + ex.getMessage());
//            throw new RuntimeException("Json processing error occurred");
//        }
//    }

    public Page<PropertySearchDTO> searchPropertiesForAdmin(PropertySearchRequest request, Pageable pageable) {
        String cacheKey = generateCacheKey(request, pageable);
        try {
            String cachedData = redisTemplate.opsForValue().get(cacheKey);
            if (cachedData != null) {
                return deserializePageData(cachedData);
            }
            return fetchAndCacheData(request, pageable, cacheKey);

        } catch (Exception ex) {
            log.error("Error occurred while processing property search: {}", ex.getMessage(), ex);
            // fallback to direct database query in case of cache related err
            return fetchPropertiesForAdmin(request, pageable);
        }
    }

    private String generateCacheKey(PropertySearchRequest request, Pageable pageable) {
        return String.format(CACHE_KEY_PATTERN,
            request.toString(),
            pageable.getPageNumber(),
            pageable.getPageSize());
    }

    private Page<PropertySearchDTO> deserializePageData(String cachedData) throws JsonProcessingException {
        JavaType pageType = objectMapper.getTypeFactory()
            .constructParametricType(Page.class, PropertySearchDTO.class);
        return objectMapper.readValue(cachedData, pageType);
    }

    private Page<PropertySearchDTO> fetchAndCacheData(
        PropertySearchRequest request,
        Pageable pageable,
        String cacheKey) throws JsonProcessingException
    {
        Page<PropertySearchDTO> result = fetchPropertiesForAdmin(request, pageable);

        // Cache res
        String jsonData = objectMapper.writeValueAsString(result);
        redisTemplate.opsForValue().set(cacheKey, jsonData, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
        return result;
    }

    private Page<PropertySearchDTO> fetchPropertiesForAdmin(PropertySearchRequest request, Pageable pageable) {
        return propertyRepository.findPropertiesForAdmin(request, pageable)
            .map(propertyMapper::toPropertySearchDTO);
    }

//    public void evictCache(PropertySearchRequest request, Pageable pageable) {
//        String cacheKey = generateCacheKey(request, pageable);
//        redisTemplate.delete(cacheKey);
//    }

    public Page<PropertySearchProjection> searchPropertiesForClient(PropertySearchRequest request, Pageable pageable) {
        return propertyRepository.findPropertiesForClient(request, pageable);
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
    public Page<PropertySearchDTO> findAllPropertiesByAccount(String accountId, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByAccountIdAndDeleted(accountId, false, pageable);
        return properties.map(propertyMapper::toPropertySearchDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertiesHomeDTO> findPropertiesByStatus(StatusEnum status, Pageable pageable) {
        Page<Property> properties = propertyRepository.findByStatus(status, pageable);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Transactional
    @Override
    public void createProperty(PropertyCreateRequest request, List<MultipartFile> imageFiles) {
        try {
            propertyValidator.checkValidPropertyCreateRequest(request);
            Property property = propertyConverter.toPropertyEntity(request, imageFiles, false);

            Property savedProperty;
            try {
                savedProperty = propertyRepository.save(property);
                System.out.println("Property saved successfully with ID: " + savedProperty.getPropertyId());
            } catch (Exception e) {
                System.err.println("Error occurred while saving Property: " + e.getMessage());
                throw new RuntimeException("Failed to save Property", e);
            }

            PropertyServicePackageCreateRequest packageRequest = PropertyServicePackageCreateRequest.builder()
                .propertyId(savedProperty.getPropertyId())
                .packageType(request.packageType())
                .status(Status.ACTIVE)
                .postServiceIds(request.postServiceIds())
                .build();

            ResponseData response = propertyClient.createPropertyServicePackage(packageRequest).getBody();

            if (response == null || !response.getDesc().equals("Property service package created successfully")) {
                throw new RuntimeException("Failed to create Property Service Package");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
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
            .orElseThrow(() -> new NoSuchElementException("Property with id " + id + " not found or has been deleted."));
    }

    @Transactional(readOnly = true)
    public Property findPropertyExistedById(Long id) {
        return propertyRepository.findByPropertyIdAndDeleted(id, false)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + id));
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

//    @Transactional
//    protected void updatePropertyDetails(Property existingProperty, PropertyUpdateRequest request) {
//        if (request != null) {
//            updateExistingPropertyRooms(existingProperty, request.rooms()); // update rooms info separately
//            propertyMapper.updatePropertyFromDto(request, existingProperty);
//        }
//    }

    @Transactional
    protected void updatePropertyDetails(Property existingProperty, PropertyUpdateRequest request) {
        if (request != null) {
            updateExistingPropertyRooms(existingProperty, request.rooms());

            PropertyForRent propertyForRent = null;
            PropertyForSale propertyForSale = null;

            if (request.propertyForRent() != null) {
                propertyForRent = PropertyForRent.builder()
                    .property(existingProperty)
                    .propertyId(existingProperty.getPropertyId())
                    .rentalPrice(request.propertyForRent().rentalPrice())
                    .paymentSchedule(request.propertyForRent().paymentSchedule())
                    .rentalTerms(request.propertyForRent().rentalTerms())
                    .build();
            }

            if (request.propertyForSale() != null) {
                propertyForSale = PropertyForSale.builder()
                    .property(existingProperty)
                    .propertyId(existingProperty.getPropertyId())
                    .salePrice(request.propertyForSale().salePrice())
                    .saleTerms(request.propertyForSale().saleTerms())
                    .build();
            }

            Address updatedAddress = Address.builder()
                .property(existingProperty)
                .id(existingProperty.getAddress() != null ? existingProperty.getAddress().getId() : null)
                .city(request.address().city())
                .district(request.address().district())
                .ward(request.address().ward())
                .streetAddress(request.address().streetAddress())
                .build();

            existingProperty.setTitle(request.title());
            existingProperty.setCategoryId(Math.toIntExact(request.categoryId()));
            existingProperty.setAccountId(request.accountId());
            existingProperty.setArea(
                BigDecimal.valueOf(request.area() != null ? request.area() : existingProperty.getArea().doubleValue())
            );
            existingProperty.setDescription(request.description());
            existingProperty.setTotalFloor(request.totalFloor() != null ? request.totalFloor().shortValue() : null);
            existingProperty.setStatus(request.status());
            existingProperty.setHouseDirection(request.houseDirection());
            existingProperty.setBalconyDirection(request.balconyDirection());
            existingProperty.setPropertyForRent(propertyForRent);
            existingProperty.setPropertyForSale(propertyForSale);
            existingProperty.setAddress(updatedAddress);
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
    public void updatePropertyStatus(Long id, PropertyStatusUpdateRequest request) {
        Property existedCategory = propertyRepository.findByPropertyIdAndDeleted(id, false)
            .orElseThrow(() -> new EntityNotFoundException("Property category not existed with id: " + id));

        existedCategory.setStatus(request.status());
        propertyRepository.save(existedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean propertyActiveCheck(Long id) {
        return propertyRepository.existsByPropertyIdAndDeletedAndStatus(id, false, StatusEnum.ACTIVE);
    }
}
