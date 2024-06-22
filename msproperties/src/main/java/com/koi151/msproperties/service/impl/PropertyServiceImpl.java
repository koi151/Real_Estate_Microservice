package com.koi151.msproperties.service.impl;

import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.*;
import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.model.request.PropertyCreateRequest;
import com.koi151.msproperties.model.request.PropertySearchRequest;
import com.koi151.msproperties.model.request.PropertyUpdateRequest;
import com.koi151.msproperties.repository.*;
import com.koi151.msproperties.service.PropertiesService;
import com.koi151.msproperties.service.converter.PropertyConverter;
import customExceptions.MaxImagesExceededException;
import customExceptions.PropertyNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertiesService {

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    CloudinaryServiceImpl cloudinaryServiceImpl;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PropertyConverter propertyConverter;

    @Autowired
    PropertyForSaleRepository propertyForSaleRepository;

    @Autowired
    PropertyForRentRepository propertyForRentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<PropertySearchDTO> findAllProperties(PropertySearchRequest request) {
        List<PropertyEntity> propertyEntities = propertyRepository.findPropertiesByCriteria(request);
        List<PropertySearchDTO> result = new ArrayList<>();

        for (PropertyEntity item : propertyEntities) {
            PropertySearchDTO property = propertyConverter.toPropertySearchDTO(item);
            result.add(property);
        }

        return result;
    }

    @Override
    public List<PropertiesHomeDTO> getHomeProperties(Map<String, Object> params) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        Page<PropertyEntity> properties = propertyRepository.findByDeleted(false, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatus(), property.getView()))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyEntity getPropertyById(Integer id) {
        return propertyRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + id));
    }

    @Override
    public List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<PropertyEntity> properties = propertyRepository.findByCategoryIdAndDeleted(categoryId, false, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatus(), property.getView()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<PropertyEntity> properties = propertyRepository.findByStatus(status, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatus(), property.getView()))
                .collect(Collectors.toList());
    }

    @Override
    public FullPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> images) {
        AddressEntity addressEntity = propertyConverter.toAddressEntity(request.getAddress());
        addressRepository.save(addressEntity);

        PropertyEntity propertyEntity = propertyConverter.toPropertyEntity(request, images);
        propertyEntity.setAddressEntity(addressEntity);

        propertyRepository.save(propertyEntity);
        return propertyConverter.toFullPropertyDTO(propertyEntity);
    }

//    @Override
//    public FullPropertyDTO createProperty(PropertyCreateRequest request, List<MultipartFile> imageFiles) {
//        AddressEntity addressEntity = new AddressEntity(
//                request.getAddress().getCity(),
//                request.getAddress().getDistrict(),
//                request.getAddress().getWard(),
//                request.getAddress().getStreetAddress()
//        );
//
//        // Saved address first since its dependent ------------------------------------------------------------
//        addressRepository.save(addressEntity);
//
//        PropertyEntity propertyEntity = PropertyEntity.builder()
//                .title(request.getTitle())
//                .categoryId(request.getCategoryId())
//                .addressEntity(addressEntity)
//                .area(request.getArea())
//                .description(request.getDescription())
//                .totalFloor(request.getTotalFloor())
//                .houseDirection(request.getHouseDirection())
//                .balconyDirection(request.getBalconyDirection())
//                .status(request.getStatus())
//                .availableFrom(request.getAvailableFrom())
//                .build();
//
//        if (imageFiles != null) {
//            String imageUrls = cloudinaryServiceImpl.uploadFiles(imageFiles, "real_estate_properties");
//            if (imageUrls == null || imageUrls.isEmpty())
//                throw new RuntimeException("Failed to upload images to Cloudinary");
//
//            propertyEntity.setImageUrls(imageUrls);
//        }
//
//
//        // Save the properties entity second to ensure it is persistent --------------------------------
//        propertyRepository.save(propertyEntity);
//
//        List<RoomEntity> roomEntities = request.getRooms().stream()
//                .map(roomCreateRequest -> {
//                    RoomEntity roomEntity = new RoomEntity();
//                    roomEntity.setPropertyEntity(propertyEntity);
//                    roomEntity.setRoomType(roomCreateRequest.getRoomType());
//                    roomEntity.setQuantity(roomCreateRequest.getQuantity());
//                    return roomEntity;
//                })
//                .toList();
//
//        // Save multiple room at once --------------------------------------------------
//        roomRepository.saveAll(roomEntities);
//
//        // Save PropertyForRent or PropertyForSale base on type, validated
//        if(request.getType() == PropertyTypeEnum.RENT) {
//            PropertyForRentEntity propertyForRentEntity = PropertyForRentEntity.builder()
//                    .property_id(propertyEntity.getId())
//                    .propertyEntity(propertyEntity)
//                    .rentalPrice(request.getPrice())
//                    .paymentSchedule(request.getPaymentSchedule())
//                    .rentTerm(request.getTerm())
//                    .build();
//
//            propertyForRentRepository.save(propertyForRentEntity);
//
//        } else {
//            PropertyForSaleEntity propertyForSaleEntity = PropertyForSaleEntity.builder()
//                    .propertyId(propertyEntity.getId())
//                    .salePrice(request.getPrice())
//                    .propertyEntity(propertyEntity)
//                    .saleTerm(request.getTerm())
//                    .build();
//
//            propertyForSaleRepository.save(propertyForSaleEntity);
//        }
//
//        return FullPropertyDTO.builder()
//                .title(propertyEntity.getTitle())
//                .categoryId(propertyEntity.getCategoryId())
//                .price(request.getPrice())
//                .area(propertyEntity.getArea())
//                .description(propertyEntity.getDescription())
//                .totalFloor(propertyEntity.getTotalFloor())
//                .houseDirection(propertyEntity.getHouseDirection())
//                .balconyDirection(propertyEntity.getBalconyDirection())
//                .status(propertyEntity.getStatus())
//                .availableFrom(propertyEntity.getAvailableFrom())
//                .imageUrls(propertyEntity.getImageUrls() != null && !propertyEntity.getImageUrls().isEmpty()
//                        ? Arrays.stream(propertyEntity.getImageUrls().split(","))
//                            .map(String::trim)
//                            .collect(Collectors.toList())
//                        : Collections.emptyList())
//                .rooms(roomEntities.stream()
//                        .map(roomEntity -> new RoomDTO(roomEntity.getRoomId(), propertyEntity.getId(), roomEntity.getRoomType(), roomEntity.getQuantity()))
//                        .collect(Collectors.toList()))
//                .build();
//    }

    @Override
    public FullPropertyDTO updateProperty(Integer id, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
//        return propertyRepository.findByIdAndDeleted(id, false)
//                .map(existingProperty -> {
//                    updatePropertyDetails(existingProperty, request);
//                    updateImages(existingProperty, request, imageFiles);
//
//                    return propertyRepository.save(existingProperty);
//                })
//                .map(savedProperty -> convertToPropertyDTO(savedProperty, request))
//                .orElseThrow(() -> new PropertyNotFoundException("Cannot find account with id: " + id));

        return null;
    }

    private void updateImages(PropertyEntity existingProperty, PropertyUpdateRequest request, List<MultipartFile> imageFiles) {
        Set<String> existingImagesUrlSet = new HashSet<>();

        // Initialize existingImagesUrlSet with current image URLs if they exist
        if (existingProperty.getImageUrls() != null && !existingProperty.getImageUrls().isEmpty()) {
            existingImagesUrlSet = new HashSet<>(Arrays.asList(existingProperty.getImageUrls().split(",")));
        }

        // Handle image removal
        if (request != null && request.getImageUrlsRemove() != null && !request.getImageUrlsRemove().isEmpty()) {
            existingImagesUrlSet.removeAll(request.getImageUrlsRemove());
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

            // Use Optional for Null check
            Optional.ofNullable(request.getTitle()).ifPresent(existingProperty::setTitle);
            Optional.ofNullable(request.getDescription()).ifPresent(existingProperty::setDescription);
            Optional.ofNullable(request.getCategoryId()).ifPresent(existingProperty::setCategoryId);
            Optional.ofNullable(request.getArea()).ifPresent(existingProperty::setArea);

            Optional.ofNullable(request.getBalconyDirection()).ifPresent(existingProperty::setBalconyDirection);
            Optional.ofNullable(request.getHouseDirection()).ifPresent(existingProperty::setHouseDirection);
            Optional.ofNullable(request.getAvailableFrom()).ifPresent(existingProperty::setAvailableFrom);
            Optional.ofNullable(request.getStatus()).ifPresent(existingProperty::setStatus);

//            existingProperty.setUpdatedAt(LocalDateTime.now());
        }
    }

//    private FullPropertyDTO convertToPropertyDTO(PropertyEntity savedProperty, PropertyUpdateRequest request) {
//
//        // check if new price update request exists, otherwise get the current price
//        Float price = (request != null && request.getPrice() != null) ? request.getPrice() :
//                (savedProperty.getPropertyForRentEntity() != null ? savedProperty.getPropertyForRentEntity().getRentalPrice() :
//                        savedProperty.getPropertyForSaleEntity() != null ? savedProperty.getPropertyForSaleEntity().getSalePrice() : null);
//
//        return FullPropertyDTO.builder()
//                .title(savedProperty.getTitle())
//                .categoryId(savedProperty.getCategoryId())
//                .price(price)
//                .area(savedProperty.getArea())
//                .description(savedProperty.getDescription())
//                .totalFloor(savedProperty.getTotalFloor())
//                .houseDirection(savedProperty.getHouseDirection().getDirectionName())
//                .balconyDirection(savedProperty.getBalconyDirection().getDirectionName())
//                .status(savedProperty.getStatus().getStatusName())
//                .availableFrom(savedProperty.getAvailableFrom())
//                .imageUrls(savedProperty.getImageUrls() != null && !savedProperty.getImageUrls().isEmpty()
//                        ? Arrays.stream(savedProperty.getImageUrls().split(","))
//                        .map(String::trim)
//                        .collect(Collectors.toList())
//                        : Collections.emptyList())
//                .rooms(savedProperty.getRoomEntities().stream()
//                        .map(roomEntity -> new RoomNameQuantityDTO(roomEntity.getRoomType(), roomEntity.getQuantity()))
//                        .collect(Collectors.toList()))
//                .build();
//    }


    @Override
    public void deleteProperty(Integer id) throws PropertyNotFoundException {
        propertyRepository.findById(id)
                .map(existingProperty -> {
                    existingProperty.setDeleted(true);
                    return propertyRepository.save(existingProperty);
                })
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
    }
}
