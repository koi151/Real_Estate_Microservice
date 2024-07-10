package com.koi151.msproperties.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.model.dto.FullPropertyDTO;
import com.koi151.msproperties.model.dto.PropertiesHomeDTO;
import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.PropertySearchDTO;
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
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestBody @Valid PropertySearchRequest request) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdDate"));
        var propertiesPage = propertiesService.findAllProperties(request, pageable);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertiesPage.getContent());
        responseData.setDesc(propertiesPage.isEmpty()
                ? "No property found"
                : String.format("Get properties succeed. Page: %d. Total %d properties", propertiesPage.getNumber() + 1, propertiesPage.getTotalElements()));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/generateFakeProperties")
    private ResponseEntity<ResponseData> generateFakeProperties() {
        Faker faker = new Faker();

        for (int i = 0; i < 2000; i++) {

            // Generate fake address
            AddressCreateRequest address = AddressCreateRequest.builder()
                    .city(faker.address().city())
                    .district(faker.address().cityName())
                    .ward(faker.address().streetName())
                    .streetAddress(faker.address().streetAddress())
                    .build();

            String adjective = faker.commerce().material();
            String realEstateType = faker.commerce().department();
            String realEstateTitle = adjective + " " + realEstateType;

            // Ensure at least one of propertyForSale or propertyForRent is present
            boolean createForSale = faker.bool().bool();
            boolean createForRent = faker.bool().bool() || !createForSale; // Ensure at least one is true

            PropertyForSaleCreateRequest propertyForSale = null;
            if (createForSale) {
                propertyForSale = PropertyForSaleCreateRequest.builder()
                        .salePrice(faker.number().randomDouble(2, 5000, 20_000_000))
                        .saleTerm(faker.lorem().sentence())
                        .build();
            }

            PropertyForRentCreateRequest propertyForRent = null;
            PaymentScheduleEnum[] paymentSchedules = PaymentScheduleEnum.values();
            int randomPaymentScheduleIndex = faker.number().numberBetween(0, paymentSchedules.length);

            if (createForRent) {
                propertyForRent = PropertyForRentCreateRequest.builder()
                        .rentalPrice(faker.number().randomDouble(2, 500, 300000))
                        .rentalTerm(faker.lorem().sentence())
                        .paymentSchedule(paymentSchedules[randomPaymentScheduleIndex])
                        .build();
            }

            DirectionEnum[] directions = DirectionEnum.values();
            StatusEnum[] statuses = StatusEnum.values();

            int randomDir1Index = faker.number().numberBetween(0, directions.length);
            int randomDir2Index = faker.number().numberBetween(0, directions.length);
            int randomStatusIndex = faker.number().numberBetween(0, statuses.length);

            // Generate fake availableFrom date
            String availableFrom = String.format("%02d/%02d",
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28));

            var propertyDTO = PropertyCreateRequest.builder()
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
                    .build();

                propertiesService.createProperty(propertyDTO, null);
        }

        ResponseData responseData = new ResponseData();
        responseData.setDesc("Fake Properties created successfully");

        return ResponseEntity.ok(responseData);
    }




    @GetMapping("/home")
    public ResponseEntity<?> findHomeProperties(@RequestParam Map<String, Object> params) {
        List<PropertiesHomeDTO> properties = propertiesService.getHomeProperties(params);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No property found"
                : "Get properties succeed");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData> findPropertyById(@PathVariable(name = "id") Long id) {
        PropertyEntity propertyEntity = propertiesService.getPropertyById(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(propertyEntity);
        responseData.setDesc("Success");

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/category/{category-id}")
    public ResponseEntity<ResponseData> getPropertiesByCategory(@PathVariable(name="category-id") Integer categoryId) {
        List<PropertiesHomeDTO> properties = propertiesService.findAllPropertiesByCategory(categoryId);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty() ?
                "No properties found with id: " + categoryId : "Success");

        return ResponseEntity.ok(responseData);
    }

//    @GetMapping("/account/{account-id}")
//    public ResponseEntity<ResponseData> getPropertiesByAccount(@PathVariable(name = "account-id") Long accountId) {
//        var properties = propertiesService.
//
//    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseData> findPropertiesWithStatus(@PathVariable(name = "status") String status) {
        StatusEnum se = StatusEnum.valueOf(status.toUpperCase());
        List<PropertiesHomeDTO> properties = propertiesService.getPropertiesWithStatus(se);

        ResponseData responseData = new ResponseData();
        responseData.setData(properties);
        responseData.setDesc(properties.isEmpty()
                ? "No properties found with status " + status
                : "Success");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData> createProperty(
            @RequestPart @Valid PropertyCreateRequest property,
            @RequestPart(required = false) List<MultipartFile> images
    ) {

//        if (property.getPropertyForRent() && property.getPropertyForRent().getRentalPrice() == )
//            throw new PaymentScheduleNotFoundException("Payment schedule required in property for sale");

        FullPropertyDTO propertyRes = propertiesService.createProperty(property, images);

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
