package com.koi151.msproperties.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.model.dto.DetailedPropertyDTO;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.reponse.ResponseData;
import com.koi151.msproperties.model.request.*;
import com.koi151.msproperties.service.PropertiesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/properties")
@PropertySource("classpath:application.yml")
public class PropertyController {

    @Autowired
    PropertiesService propertiesService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/")
    public ResponseEntity<ResponseData> findAllProperties (
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestBody @Valid PropertySearchRequest request) {

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate"));
        var propertiesPage = propertiesService.findAllProperties(request, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage.getContent());
        responseData.setCurrentPage(page);
        responseData.setMaxPageItems(limit);
        responseData.setTotalItems(propertiesPage.getTotalElements());
        responseData.setTotalPages(propertiesPage.getTotalPages());

        responseData.setDesc(propertiesPage.isEmpty()
                ? "No property found"
                : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/generateFakeProperties")
    private ResponseEntity<ResponseData> generateFakeProperties() {
        Faker faker = new Faker();

        DirectionEnum[] directions = DirectionEnum.values();
        StatusEnum[] statuses = StatusEnum.values();
        PaymentScheduleEnum[] paymentSchedules = PaymentScheduleEnum.values();

        propertiesService.createFakeProperties(IntStream.range(0, 2000)
                .mapToObj(i -> {
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
                            .salePrice(faker.number().randomDouble(2, 5000, 20_000_000))
                            .saleTerm(faker.lorem().sentence())
                            .build() : null;

                    PropertyForRentCreateRequest propertyForRent = createForRent ? PropertyForRentCreateRequest.builder()
                            .rentalPrice(faker.number().randomDouble(2, 500, 300000))
                            .rentalTerm(faker.lorem().sentence())
                            .paymentSchedule(paymentSchedules[faker.number().numberBetween(0, paymentSchedules.length)])
                            .build() : null;

                    int randomDir1Index = faker.number().numberBetween(0, directions.length);
                    int randomDir2Index = faker.number().numberBetween(0, directions.length);
                    int randomStatusIndex = faker.number().numberBetween(0, statuses.length);

                    String availableFrom = String.format("%02d/%02d", faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28));

                    return FakePropertyCreateRequest.builder()
                            .title(realEstateTitle)
                            .propertyForSale(propertyForSale)
                            .propertyForRent(propertyForRent)
                            .accountId((long) faker.number().numberBetween(1, 7))
                            .address(address)
                            .area((float) faker.number().randomDouble(2, 10, 1200))
                            .description(faker.lorem().paragraph())
                            .totalFloor(faker.number().numberBetween(0, 20))
                            .categoryId((long) faker.number().numberBetween(1, 5))
                            .houseDirection(directions[randomDir1Index])
                            .balconyDirection(directions[randomDir2Index])
                            .status(statuses[randomStatusIndex])
                            .availableFrom(availableFrom)
                            .view(faker.number().numberBetween(0, 20000))
                            .build();
                })
                .collect(Collectors.toList())); // Collect the stream into a list

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Fake properties created successfully");
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/home")
    public ResponseEntity<?> findHomeProperties(
            @RequestParam Map<String, Object> params,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());

        var propertiesPage = propertiesService.getHomeProperties(params, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage);
        responseData.setCurrentPage(page);
        responseData.setTotalPages(propertiesPage.getTotalPages());
        responseData.setMaxPageItems(limit);
        responseData.setTotalItems(propertiesPage.getTotalElements());
        responseData.setDesc(propertiesPage.isEmpty()
                ? "No property found"
                : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData> findPropertyById(@PathVariable(name = "id") Long id) {
        var property = propertiesService.getPropertyById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(property);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<ResponseData> getPropertiesByCategory(
            @PathVariable(name="category-id") Integer categoryId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());
        var propertiesPage = propertiesService.findAllPropertiesByCategory(categoryId, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage);
        responseData.setCurrentPage(page);
        responseData.setTotalPages(propertiesPage.getTotalPages());
        responseData.setMaxPageItems(limit);
        responseData.setTotalItems(propertiesPage.getTotalElements());

        responseData.setDesc(propertiesPage.isEmpty() ?
                "No properties found with id: " + categoryId : "Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/account/{account-id}")
    public ResponseEntity<ResponseData> findPropertiesByAccount(
            @PathVariable(name = "account-id") Long accountId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdDate").descending());
        var propertiesPage = propertiesService.findAllPropertiesByAccount(accountId, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage.getContent());
        responseData.setCurrentPage(page);
        responseData.setMaxPageItems(limit);
        responseData.setTotalItems(propertiesPage.getTotalElements());
        responseData.setTotalPages(propertiesPage.getTotalPages());
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
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());

        var propertiesPage = propertiesService.findPropertiesByStatus(se, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage);
        responseData.setCurrentPage(page);
        responseData.setMaxPageItems(limit);
        responseData.setTotalPages(propertiesPage.getTotalPages());
        responseData.setTotalItems(propertiesPage.getTotalElements());
        responseData.setDesc(propertiesPage.isEmpty()
                ? "No properties found with status " + status
                : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createProperty(
            @RequestPart @Valid PropertyCreateRequest property,
            @RequestPart(required = false) List<MultipartFile> images
    ) {

        DetailedPropertyDTO propertyRes = propertiesService.createProperty(property, images);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertyRes);
        responseData.setDesc("Property created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteProperty (@PathVariable(name = "id") Long id) {
        ResponseData responseData = new ResponseData();

        propertiesService.deleteProperty(id);
        responseData.setDesc("Property deleted successfully");

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ResponseData> updateProperty(
            @PathVariable(name = "id") Long id,
            @RequestPart(required = false) @Valid PropertyUpdateRequest property,
            @RequestPart(required = false) List<MultipartFile> images
    ){
        ResponseData responseData = new ResponseData();

        responseData.setData(propertiesService.updateProperty(id, property, images));
        responseData.setDesc("Property updated successfully");

        return ResponseEntity.ok(responseData);
    }
}
