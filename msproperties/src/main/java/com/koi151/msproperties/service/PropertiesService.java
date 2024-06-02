package com.koi151.msproperties.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.msproperties.dto.FullPropertiesDTO;
import com.koi151.msproperties.dto.PropertiesHomeDTO;
import com.koi151.msproperties.dto.RoomDTO;
import com.koi151.msproperties.entity.*;
import com.koi151.msproperties.entity.payload.request.PropertyCreateRequest;
import com.koi151.msproperties.entity.payload.request.PropertyUpdateRequest;
import com.koi151.msproperties.entity.payload.request.RoomCreateRequest;
import com.koi151.msproperties.repository.PropertiesRepository;
import com.koi151.msproperties.repository.PropertyForRentRepository;
import com.koi151.msproperties.repository.PropertyForSaleRepository;
import com.koi151.msproperties.repository.RoomRepository;
import com.koi151.msproperties.service.imp.PropertiesServiceImp;
import customExceptions.PaymentScheduleNotFoundException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertiesService implements PropertiesServiceImp {

    @Autowired
    PropertiesRepository propertiesRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    PropertyForSaleRepository propertyForSaleRepository;

    @Autowired
    PropertyForRentRepository propertyForRentRepository;

    @Autowired
    ObjectMapper objectMapper; // For parsing JSON strings

    @Override
    public List<PropertiesHomeDTO> getHomeProperties() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findByDeleted(false, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatusEnum(), property.getView()))
                .collect(Collectors.toList());
    }


    @Override
    public Properties getPropertyById(Integer id) {
        return propertiesRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new PropertyNotFoundException("No property found with id " + id));
    }


    @Override
    public List<PropertiesHomeDTO> findAllPropertiesByCategory(Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findByCategoryIdAndDeleted(categoryId, false, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatusEnum(), property.getView()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertiesHomeDTO> getPropertiesWithStatus(StatusEnum status) {
        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by("id"));
        Page<Properties> properties = propertiesRepository.findByStatusEnum(status, pageRequest);

        return properties.stream()
                .map(property -> new PropertiesHomeDTO(property.getTitle(), property.getImageUrls(), property.getDescription(), property.getStatusEnum(), property.getView()))
                .collect(Collectors.toList());
    }

    @Override
    public FullPropertiesDTO createProperty(PropertyCreateRequest request) {

        if (request.getType() == PropertyTypeEnum.RENT && request.getPaymentSchedule() == null)
            throw new PaymentScheduleNotFoundException("Payment schedule required in property for sale");

        Properties properties = Properties.builder()
                .title(request.getTitle())
                .categoryId(request.getCategoryId())
                .area(request.getArea())
                .description(request.getDescription())
                .totalFloor(request.getTotalFloor())
                .houseDirectionEnum(request.getHouseDirection())
                .balconyDirectionEnum(request.getBalconyDirection())
                .statusEnum(request.getStatus())
                .availableFrom(request.getAvailableFrom())
                .updatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String imageUrls = cloudinaryService.uploadFile(request.getImages(), "real_estate_properties");
            if (imageUrls == null || imageUrls.isEmpty()) {
                throw new RuntimeException("Failed to upload image to Cloudinary");
            }
            properties.setImageUrls(imageUrls);
        }

        // Save the properties entity first to ensure it is persistent
        propertiesRepository.save(properties);

        // Save PropertyForRent or PropertyForSale base on type, validated
        if(request.getType() == PropertyTypeEnum.RENT) {
            PropertyForRent propertyForRent = PropertyForRent.builder()
                    .property_id(properties.getId())
                    .properties(properties)
                    .rentalPrice(request.getPrice())
                    .paymentSchedule(request.getPaymentSchedule())
                    .rentTerm(request.getTerm())
                    .build();

            propertyForRentRepository.save(propertyForRent);

        } else {
            PropertyForSale propertyForSale = PropertyForSale.builder()
                    .propertyId(properties.getId())
                    .salePrice(request.getPrice())
                    .properties(properties)
                    .saleTerm(request.getTerm())
                    .build();

            propertyForSaleRepository.save(propertyForSale);
        }

        if (request.getRooms() != null && !request.getRooms().isEmpty()) {
            try {
                List<RoomCreateRequest> roomRequests = objectMapper.readValue(request.getRooms(), new TypeReference<List<RoomCreateRequest>>() {});
                List<Room> rooms = roomRequests.stream()
                        .map(roomRequest -> {
                            Room room = new Room();
                            room.setProperties(properties); // Associate with the already saved properties
                            room.setRoomType(roomRequest.getRoomType());
                            room.setQuantity(roomRequest.getQuantity());
                            return room;
                        })
                        .collect(Collectors.toList());

                roomRepository.saveAll(rooms);
                properties.setRoomSet(new HashSet<>(rooms));

            } catch (Exception e) {
                throw new RuntimeException("Failed to parse rooms JSON", e);
            }
        }

        return FullPropertiesDTO.builder()
                .title(properties.getTitle())
                .categoryId(properties.getCategoryId())
                .area(properties.getArea())
                .description(properties.getDescription())
                .totalFloor(properties.getTotalFloor())
                .houseDirection(properties.getHouseDirectionEnum())
                .balconyDirection(properties.getBalconyDirectionEnum())
                .status(properties.getStatusEnum())
                .availableFrom(properties.getAvailableFrom())
                .imageUrls(properties.getImageUrls())
                .rooms(properties.getRoomSet().stream()
                        .map(room -> new RoomDTO(room.getRoomId(), properties.getId(), room.getRoomType(), room.getQuantity()))
                        .collect(Collectors.toList()))
                .build();
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
