package com.koi151.msproperties.controller;

import com.github.javafaker.Faker;
import com.koi151.msproperties.enums.*;
import com.koi151.msproperties.mapper.ResponseDataMapper;
import com.koi151.msproperties.model.dto.DetailedPropertyDTO;
import com.koi151.msproperties.model.reponse.ResponseData;
import com.koi151.msproperties.model.request.address.AddressCreateRequest;
import com.koi151.msproperties.model.request.property.PropertyCreateRequest;
import com.koi151.msproperties.model.request.property.PropertySearchRequest;
import com.koi151.msproperties.model.request.property.PropertyUpdateRequest;
import com.koi151.msproperties.model.request.propertyForRent.PropertyForRentCreateRequest;
import com.koi151.msproperties.model.request.propertyForSale.PropertyForSaleCreateRequest;
import com.koi151.msproperties.model.request.propertyPostService.PropertyPostServiceCreateUpdateRequest;
import com.koi151.msproperties.model.request.rooms.RoomCreateUpdateRequest;
import com.koi151.msproperties.service.PropertiesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertiesService propertiesService;
    private final ResponseDataMapper responseDataMapper;

    private static final int MAX_PAGE_SIZE = 20;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_properties_view')")
    public ResponseEntity<ResponseData> findAllProperties (
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int limit,
        @RequestBody @Valid PropertySearchRequest request)
    {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate"));
        var propertiesPage = propertiesService.findAllProperties(request, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
        responseData.setDesc(propertiesPage.isEmpty()
            ? "No property found"
            : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/generateFakeProperties")
    @PreAuthorize("hasAuthority('SCOPE_properties_create')")
    private ResponseEntity<ResponseData> generateFakeProperties() {
        Faker faker = new Faker();
        int numberOfFakeProperties = 20000;

        propertiesService.createFakeProperties(IntStream.range(0, numberOfFakeProperties)
            .mapToObj(i -> {
                // property
                DirectionEnum[] directions = DirectionEnum.values();
                int randomDir1Index = faker.number().numberBetween(0, directions.length);
                int randomDir2Index = faker.number().numberBetween(0, directions.length);

                StatusEnum[] statuses = StatusEnum.values();
                int randomStatusIndex = faker.number().numberBetween(0, statuses.length);

                // propertyForRent
                PaymentScheduleEnum[] paymentSchedules = PaymentScheduleEnum.values();

                // propertyPostService
                PostingPackageEnum[] postingPackages = PostingPackageEnum.values();
                int randomPostPackageIndex = faker.number().numberBetween(0, postingPackages.length);

                DaysPostedEnum[] daysPostedEnums = DaysPostedEnum.values();
                int randomDaysPostedIndex = faker.number().numberBetween(0, daysPostedEnums.length);


                // Generate fake data directly in the stream
                AddressCreateRequest address = AddressCreateRequest.builder()
                        .city(faker.address().city())
                        .district(faker.address().cityName())
                        .ward(faker.address().streetName())
                        .streetAddress(faker.address().streetAddress())
                        .build();

                String realEstateTitle = faker.commerce().material() + " " + faker.commerce().department();

                // Ensure at least one property type is present
                boolean createForSale = faker.bool().bool();
                boolean createForRent = faker.bool().bool() || !createForSale;

                PropertyForSaleCreateRequest propertyForSale = createForSale ? PropertyForSaleCreateRequest.builder()
                        .salePrice(BigDecimal.valueOf(faker.number().randomDouble(2, 5000, 20_000_000)))
                        .saleTerm(faker.lorem().sentence())
                        .build() : null;

                PropertyForRentCreateRequest propertyForRent = createForRent ? PropertyForRentCreateRequest.builder()
                        .rentalPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 500, 300000)))
                        .rentalTerm(faker.lorem().sentence())
                        .paymentSchedule(paymentSchedules[faker.number().numberBetween(0, paymentSchedules.length)])
                        .build() : null;

                LocalDateTime postingDate = LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30));

                PropertyPostServiceCreateUpdateRequest postServiceCreate = PropertyPostServiceCreateUpdateRequest.builder()
                        .postingPackage(postingPackages[randomPostPackageIndex])
                        .priorityPushes((short) faker.number().numberBetween(0, 30))
                        .postingDate(postingDate)
                        .daysPosted(daysPostedEnums[randomDaysPostedIndex])
                        .build();

                // rooms
                RoomTypeEnum[] roomTypes = RoomTypeEnum.values();
                Set<RoomTypeEnum> usedRoomTypes = new HashSet<>(); // Track used room types
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

                LocalDate availableFrom = LocalDate.of(
                        2024,
                        faker.number().numberBetween(7, 9),
                        faker.number().numberBetween(1, 28)
                );

                return PropertyCreateRequest.builder()
                    .title(realEstateTitle)
                    .propertyForSale(propertyForSale)
                    .propertyForRent(propertyForRent)
                    .propertyPostService(postServiceCreate)
                    .accountId(String.valueOf(faker.number().numberBetween(1, 7)))
                    .address(address)
                    .rooms(rooms)
                    .area(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1200)))
                    .description(faker.lorem().paragraph())
                    .totalFloor((short) faker.number().numberBetween(0, 20))
                    .categoryId((long) faker.number().numberBetween(1, 5))
                    .houseDirection(directions[randomDir1Index])
                    .balconyDirection(directions[randomDir2Index])
                    .status(statuses[randomStatusIndex])
                    .availableFrom(availableFrom)
                    .build();
                })
                .collect(Collectors.toList()));

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Fake properties created successfully. Total " + numberOfFakeProperties + " properties created.");
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/home")
    @PreAuthorize("hasAuthority('SCOPE_properties_view')")
    public ResponseEntity<?> findHomeProperties(
        @RequestParam Map<String, Object> params,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());

        var propertiesPage = propertiesService.getHomeProperties(params, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
        responseData.setDesc(propertiesPage.isEmpty()
            ? "No property found"
            : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData> findPropertyById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(ResponseData.builder()
            .data(propertiesService.getPropertyById(id))
            .desc("Success")
        .build());
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<ResponseData> getPropertiesByCategory(
            @PathVariable(name="category-id") Integer categoryId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());
        var propertiesPage = propertiesService.findAllPropertiesByCategory(categoryId, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
        responseData.setDesc(propertiesPage.isEmpty() ?
                "No properties found with id: " + categoryId : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/account/{account-id}")
    public ResponseEntity<ResponseData> findAllPropertiesByAccount(
        @PathVariable(name = "account-id") String accountId,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
        var propertiesPage = propertiesService.findAllPropertiesByAccount(accountId, pageable);

        ResponseData responseData = responseDataMapper.toResponseData(propertiesPage, page, pageSize);
        responseData.setDesc(propertiesPage.isEmpty()
            ? "No property found"
            : "Get properties by account id succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> findPropertiesByStatus(
            @PathVariable(name = "status") String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());
        int pageSize = Math.min(limit, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("createdDate").descending());

        var propertiesPage = propertiesService.findPropertiesByStatus(se, pageable);

        return ResponseEntity.ok(ResponseData.builder()
            .data(responseDataMapper.toResponseData(propertiesPage, page, pageSize))
            .desc(propertiesPage.isEmpty()
                ? "No properties found with status " + status
                : "Success")
            .build()
        );
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_properties_create')")
    public ResponseEntity<ResponseData> createProperty(
            @RequestPart @Valid PropertyCreateRequest property,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseData.builder()
                .data(propertiesService.createProperty(property, images))
                .desc("Property post created successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_properties_delete')")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Long id) {
        propertiesService.deleteProperty(id);
        return ResponseEntity.ok(ResponseData.builder()
            .desc("Property deleted successfully")
            .build());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_properties_update')")
    public ResponseEntity<ResponseData> updateProperty(
        @PathVariable(name = "id") Long id,
        @RequestPart(required = false) @Valid PropertyUpdateRequest property,
        @RequestPart(required = false) List<MultipartFile> images
    ){
        return ResponseEntity.ok(ResponseData.builder()
            .data(propertiesService.updateProperty(id, property, images))
            .desc("Property updated successfully")
            .build());
    }

    @GetMapping("/{id}/active") // used for other domains application request
    public ResponseEntity<ResponseData> propertyActiveCheck(@PathVariable(name = "id") Long id) {
        var res = propertiesService.propertyActiveCheck(id);
        return ResponseEntity.ok(ResponseData.builder()
            .data(res)
            .desc("Property with id " + id + " is " + (res ? "active" : "not active or exists"))
            .build());
    }
}