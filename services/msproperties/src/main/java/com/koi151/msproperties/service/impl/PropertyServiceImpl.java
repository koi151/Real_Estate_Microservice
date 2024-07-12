package com.koi151.msproperties.service.impl;

import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.mapper.AddressMapper;
import com.koi151.msproperties.mapper.PropertyMapper;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.model.request.*;
import com.koi151.msproperties.repository.*;
import com.koi151.msproperties.service.PropertiesService;
import com.koi151.msproperties.customExceptions.MaxImagesExceededException;
import com.koi151.msproperties.customExceptions.PropertyNotFoundException;
import com.koi151.msproperties.service.converter.PropertyConverter;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertiesService {

    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final AddressRepository addressRepository;
    private final PropertyMapper propertyMapper;
    private final PropertyConverter propertyConverter;

    @Override
    public Page<PropertySearchDTO> findAllProperties(PropertySearchRequest request, Pageable pageable) {
        Page<PropertyEntity> propertyEntities = propertyRepository.findPropertiesByCriteria(request, pageable);
        return propertyEntities.map(propertyMapper::toPropertySearchDTO);  // Map to DTOs using streams
    }


    @Override
    public Page<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("propertyId"));
        Page<PropertyEntity> properties = propertyRepository.findByDeleted(false, pageRequest);

        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Override
    public PropertyEntity getPropertyById(Long id) {
        return propertyRepository.findByPropertyIdAndDeleted(id, false)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + id));
    }

    @Override
    public Page<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<PropertyEntity> properties = propertyRepository.findByCategoryIdAndDeleted(categoryId, false, pageRequest);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Override
    public Page<PropertySearchDTO> findAllPropertiesByAccount(Long accountId, Pageable pageable) {
        Page<PropertyEntity> properties = propertyRepository.findByAccountIdAndDeleted(accountId, false, pageable);
        return properties.map(propertyMapper::toPropertySearchDTO);
    }

    @Override
    public Page<PropertiesHomeDTO> findPropertiesByStatus(StatusEnum status, Pageable pageable) {
        Page<PropertyEntity> properties = propertyRepository.findByStatus(status, pageable);
        return properties.map(propertyMapper::toPropertiesHomeDTO);
    }

    @Transactional
    @Override
    public FullPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> imageFiles) {
        // Convert and save the address entity
        AddressEntity addressEntity = addressRepository.save(
                AddressMapper.INSTANCE.toAddressEntity(request.getAddress())
        );

        PropertyEntity propertyEntity = propertyConverter.toPropertyEntity(request, imageFiles, addressEntity);

        // Save the property entity again to update the relationships
        propertyRepository.save(propertyEntity);
        return propertyMapper.toFullPropertyDTO(propertyEntity);
    }

    @Transactional
    public void createFakeProperties(List<FakePropertyCreateRequest> fakeProperties) {

        // Batch save addresses to optimize database interactions
        List<AddressEntity> addressEntities = addressRepository.saveAllAndFlush(
                fakeProperties.stream()
                        .map(FakePropertyCreateRequest::getAddress)
                        .map(AddressMapper.INSTANCE::toAddressEntity)
                        .toList()
        );

        // Create property entities in parallel
        List<PropertyEntity> propertyEntities = fakeProperties.parallelStream()
                .map(fakeProperty -> {
                    AddressEntity correspondingAddress = addressEntities.get(fakeProperties.indexOf(fakeProperty));
                    return propertyConverter.toPropertyEntity(fakeProperty, null, correspondingAddress);
                })
                .toList();

        // Bulk save all property entities
        propertyRepository.saveAllAndFlush(propertyEntities);
    }

    @Transactional
    @Override
    public FullPropertyDTO updateProperty(Long id, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
        return propertyRepository.findByPropertyIdAndDeleted(id, false)
                .map(existingProperty -> {
                    updatePropertyDetails(existingProperty, request);
                    updateImages(existingProperty, request, imageFiles);

                    return propertyRepository.save(existingProperty);
                })
                .map(propertyMapper::toFullPropertyDTO)
                .orElseThrow(() -> new PropertyNotFoundException("Cannot find account with id: " + id));

    }

    private void updateImages(PropertyEntity existingProperty, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
        Set<String> existingImagesUrlSet = new HashSet<>();

        // Initialize existingImagesUrlSet with current image URLs if they exist
        if (existingProperty.getImageUrls() != null && !existingProperty.getImageUrls().isEmpty()) {
            existingImagesUrlSet = new HashSet<>(Arrays.asList(existingProperty.getImageUrls().split(",")));
        }

        // Handle image removal
        if (request != null && request.getImageUrlsRemove() != null && !request.getImageUrlsRemove().isEmpty()) {
            request.getImageUrlsRemove().forEach(existingImagesUrlSet::remove); // faster than using removeALl
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

    private void updatePropertyDetails(PropertyEntity existingProperty, PropertyUpdateRequest request) {
        if (request != null) {

            if (request.getAddress() != null) {
                AddressEntity updatedAddress = AddressMapper.INSTANCE.updateAddressEntity(request.getAddress());
                updatedAddress.setId(existingProperty.getAddress().getId()); // Keep the existing ID
                addressRepository.save(updatedAddress);
            }

            updateRooms(existingProperty, request.getRooms()); // update existing property info with request
            propertyMapper.updatePropertyFromDto(request, existingProperty);
        }
    }

    private void updateRooms(PropertyEntity existingProperty, List<RoomCreateUpdateRequest> updatedRooms) {
        List<RoomEntity> currentRooms = existingProperty.getRooms();

        for (RoomCreateUpdateRequest updatedRoom : updatedRooms) {
            String roomType = updatedRoom.getRoomType();
            int quantity = updatedRoom.getQuantity();

            RoomEntity existingRoom = currentRooms.stream()
                    .filter(room -> room.getRoomType().equals(roomType))
                    .findFirst()
                    .orElse(null);

            if (existingRoom != null) {
                // Room type exists in the property
                if (existingRoom.getQuantity() != quantity) // Quantity is different, update it
                    existingRoom.setQuantity(quantity);
                // if quantity is the same, do nothing
            } else {
                // Room type doesn't exist, create a new room
                RoomEntity newRoom = RoomEntity.builder()
                        .roomType(roomType)
                        .quantity(quantity)
                        .propertyEntity(existingProperty)
                        .build();

                roomRepository.save(newRoom);
                currentRooms.add(newRoom);
            }
        }
        existingProperty.setRooms(currentRooms);
    }

    @Override
    public void deleteProperty(Long id) throws PropertyNotFoundException {
        propertyRepository.findById(Math.toIntExact(id))
                .map(existingProperty -> {
                    existingProperty.setDeleted(true);
                    return propertyRepository.save(existingProperty);
                })
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
    }
}
