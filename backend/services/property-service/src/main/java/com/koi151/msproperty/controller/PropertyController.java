package com.koi151.msproperty.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.koi151.msproperty.enums.*;
import com.koi151.msproperty.mapper.ResponseDataMapper;
import com.koi151.msproperty.model.request.PaginationRequest;
import com.koi151.msproperty.model.request.property.PropertyFilterRequest;
import com.koi151.msproperty.model.request.property.PropertyStatusUpdateRequest;
import com.koi151.msproperty.model.response.ResponseData;
import com.koi151.msproperty.model.request.address.AddressCreateRequest;
import com.koi151.msproperty.model.request.property.PropertyCreateRequest;
import com.koi151.msproperty.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperty.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperty.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperty.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperty.service.PropertyService;
import com.koi151.msproperty.utils.PageUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final ResponseDataMapper responseDataMapper;


    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_properties_view')")
    public ResponseEntity<ResponseData> findPropertiesForAdmin(
        @Valid PropertyFilterRequest request,
        @Valid PaginationRequest pagination)
    {
        var propertiesPage = propertyService.searchPropertiesForAdmin(
            request,
            PageUtils.buildPageRequest(pagination, 6)
        );

        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage);
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_properties_view')")
    public ResponseEntity<ResponseData> findPropertyById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(ResponseData.builder()
            .data(propertyService.getPropertyById(id))
            .desc("Success")
        .build());
    }

    @PostMapping("/generateFakeProperties")
    @PreAuthorize("hasAuthority('SCOPE_properties_create')")
    public ResponseEntity<ResponseData> generateFakeProperties() {
        Faker faker = new Faker();
        int numberOfFakeProperties = 20000;

        List<Boolean> propertyTypes = new ArrayList<>();

        // Add 12000 for sale and 8000 for rent indicators
        for (int i = 0; i < 12000; i++) propertyTypes.add(true);  // true for sale
        for (int i = 0; i < 8000; i++) propertyTypes.add(false);  // false for rent

        // shuffle the list
        Collections.shuffle(propertyTypes);

        propertyService.createFakeProperties(IntStream.range(0, numberOfFakeProperties)
            .mapToObj(i -> {
                // property
                DirectionEnum[] directions = DirectionEnum.values();
                int randomDir1Index = faker.number().numberBetween(0, directions.length);
                int randomDir2Index = faker.number().numberBetween(0, directions.length);

                StatusEnum[] statuses = StatusEnum.values();
                int randomStatusIndex = faker.number().numberBetween(0, statuses.length);

                PaymentScheduleEnum[] paymentSchedules = PaymentScheduleEnum.values();
                LegalDocumentEnum[] legalDocuments = LegalDocumentEnum.values();
                FurnitureEnum[] furnitures = FurnitureEnum.values();

                // Generate address
                AddressCreateRequest address = AddressCreateRequest.builder()
                    .city(faker.address().city())
                    .district(faker.address().cityName())
                    .ward(faker.address().streetName())
                    .streetAddress(faker.address().streetAddress())
                    .build();

                String realEstateTitle = faker.commerce().material() + " " + faker.commerce().department();

                // Determine property type based on randomized list
                PropertyForSaleCreateRequest propertyForSale = null;
                PropertyForRentCreateRequest propertyForRent = null;

                if (propertyTypes.get(i)) {
                    propertyForSale = PropertyForSaleCreateRequest.builder()
                        .salePrice(BigDecimal.valueOf(faker.number().randomDouble(1, 200, 1000000)))
                        .saleTerm(faker.lorem().sentence())
                        .build();
                } else {
                    propertyForRent = PropertyForRentCreateRequest.builder()
                        .rentalPrice(BigDecimal.valueOf(faker.number().randomDouble(1, 3, 500)))
                        .rentalTerm(faker.lorem().sentence())
                        .paymentSchedule(paymentSchedules[faker.number().numberBetween(0, paymentSchedules.length)])
                        .build();
                }

                RoomTypeEnum[] roomTypes = RoomTypeEnum.values();
                Set<RoomTypeEnum> usedRoomTypes = new HashSet<>();
                List<RoomCreateUpdateRequest> rooms = new ArrayList<>();

                int numRooms = faker.number().numberBetween(1, 4);
                for (int j = 0; j < numRooms; j++) {
                    RoomTypeEnum randomRoomType;
                    do {
                        int randomRoomTypeIndex = faker.number().numberBetween(0, roomTypes.length);
                        randomRoomType = roomTypes[randomRoomTypeIndex];
                    } while (usedRoomTypes.contains(randomRoomType));

                    usedRoomTypes.add(randomRoomType);

                    rooms.add(RoomCreateUpdateRequest.builder()
                        .roomType(randomRoomType)
                        .quantity((short) faker.number().numberBetween(1, 10))
                        .build());
                }

                return PropertyCreateRequest.builder()
                    .title(realEstateTitle)
                    .propertyForSale(propertyForSale)
                    .propertyForRent(propertyForRent)
                    .accountId(String.valueOf(faker.number().numberBetween(1, 7)))
                    .address(address)
                    .rooms(rooms)
                    .area(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1200)))
                    .description(faker.lorem().paragraph())
                    .totalFloor((short) faker.number().numberBetween(0, 20))
                    .categoryId((long) faker.number().numberBetween(1, 6))
                    .houseDirection(directions[randomDir1Index])
                    .balconyDirection(directions[randomDir2Index])
                    .status(statuses[randomStatusIndex])
                    .legalDocument(legalDocuments[faker.number().numberBetween(0, legalDocuments.length)])
                    .furniture(furnitures[faker.number().numberBetween(0, furnitures.length)])
                    .build();
            })
            .collect(Collectors.toList()));

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Successfully generated " + numberOfFakeProperties + " properties");
        return ResponseEntity.ok(responseData);
    }


//    @GetMapping("/account/{account-id}")
//    public ResponseEntity<ResponseData> findAllPropertiesByAccount(
//        @PathVariable(name = "account-id") String accountId,
//        @RequestParam(required = false, defaultValue = "0") int page,
//        @RequestParam(required = false, defaultValue = "10") int limit
//    ) {
//        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
//        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
//        var propertiesPage = propertyService.findAllPropertiesByAccount(accountId, pageable);
//
//        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
//        responseData.setDesc(propertiesPage.isEmpty()
//            ? "No property found"
//            : "Get properties by account id succeed");
//
//        return ResponseEntity.ok(responseData);
//    }

//    @GetMapping("/status/{status}")
//    public ResponseEntity<ResponseData> findPropertiesByStatus(
//            @PathVariable(name = "status") String status,
//            @RequestParam(required = false, defaultValue = "1") int page,
//            @RequestParam(required = false, defaultValue = "10") int limit
//    ) {
//        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());
//        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
//        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());
//
//        var propertiesPage = propertyService.findPropertiesByStatus(se, pageable);
//
//        return ResponseEntity.ok(ResponseData.builder()
//            .data(responseDataMapper.toResponseData(propertiesPage, page, pageSize))
//            .desc(propertiesPage.isEmpty()
//                ? "No properties found with status " + status
//                : "Success")
//            .build()
//        );
//    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_properties_create')")
    public ResponseEntity<ResponseData> createProperty(
        @RequestPart("property") String propertyJson,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PropertyCreateRequest property = objectMapper.readValue(propertyJson, PropertyCreateRequest.class);
        propertyService.createProperty(property, images);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseData.builder()
                .desc("Property post created successfully with pending status")
                .build());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_properties_update')")
    public ResponseEntity<ResponseData> updateProperty(
        @PathVariable(name = "id") Long id,
        @RequestPart("property") String propertyJson,
        @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PropertyUpdateRequest property = objectMapper.readValue(propertyJson, PropertyUpdateRequest.class);

        return ResponseEntity.ok(ResponseData.builder()
            .data(propertyService.updateProperty(id, property, images))
            .desc("Updated successfully property with id: " + id)
            .build());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_properties_delete')")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(ResponseData.builder()
            .desc("Property deleted successfully")
            .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseData> updatePropertyStatus(
        @PathVariable Long id,
        @RequestBody @Valid PropertyStatusUpdateRequest request
    ){
        propertyService.updatePropertyStatus(id, request);
        return ResponseEntity.ok(ResponseData.builder()
            .desc("Property status updated successfully")
            .build()
        );
    }


    @GetMapping("/{id}/active") // used for other domains application request
    public ResponseEntity<ResponseData> propertyActiveCheck(@PathVariable(name = "id") Long id) {
        var res = propertyService.propertyActiveCheck(id);
        return ResponseEntity.ok(ResponseData.builder()
            .data(res)
            .desc("Property with id " + id + " is " + (res ? "active" : "not active or exists"))
            .build());
    }
}