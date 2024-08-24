package com.koi151.listing_services.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.listing_services.customExceptions.EntityNotFoundCustomException;
import com.koi151.listing_services.customExceptions.FailedToDeserializingData;
import com.koi151.listing_services.entity.PostServicePackage;
import com.koi151.listing_services.entity.PropertyServicePackage;
import com.koi151.listing_services.entity.keys.PostServicePackageKey;
import com.koi151.listing_services.mapper.PostServiceMapper;
import com.koi151.listing_services.mapper.PropertyServicePackageMapper;
import com.koi151.listing_services.model.dto.PropertyServicePackageSummaryDTO;
import com.koi151.listing_services.model.dto.PostServiceBasicInfoDTO;
import com.koi151.listing_services.model.dto.PropertyServicePackageCreateDTO;
import com.koi151.listing_services.model.request.PropertyServicePackageCreateRequest;
import com.koi151.listing_services.repository.*;
import com.koi151.listing_services.service.PropertyServicePackageService;
import com.koi151.listing_services.validator.PostServiceValidate;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServicePackageServiceImpl implements PropertyServicePackageService {

    private final PropertyServicePackageRepository propertyServicePackageRepository;
    private final PostServiceRepository postServiceRepository;
    private final PostServicePackageRepository postServicePackageRepository;
    private final PostServicePricingRepository postServicePricingRepository;
    private final PromotionRepository promotionRepository;

    private final PropertyServicePackageMapper propertyServicePackageMapper;
    private final PostServiceMapper postServiceMapper;
    private final PostServiceValidate postServiceValidate;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPropertyServicePackage(PropertyServicePackageCreateRequest request) {
        // Validate the request
        postServiceValidate.postServiceCreateValidate(request);

        // Create and save PropertyServicePackage entity from request
        PropertyServicePackage propertyServicePackage = propertyServicePackageMapper.toPropertyServicePackageEntity(request);

        // Calculate the total fee of services used
        var totalFee = request.postServiceIds().stream()
            .map(postServiceId -> {
                // Fetch the standard price using Optional
                BigDecimal standardPrice = postServicePricingRepository
                    .findPriceByPostServiceIdAndPackageType(postServiceId, request.packageType())
                    .orElseThrow(() -> new EntityNotFoundCustomException(
                        "Price not found for post service ID: " + postServiceId + " and package type: " + request.packageType()
                    ));

                // Fetch discount information using Optional
                Optional<Tuple> discountInfo = promotionRepository.findPromotionInfoByPostServiceIdAndPackageType(postServiceId, request.packageType());

                // Initialize discount values
                BigDecimal discountPercentage = BigDecimal.ZERO;
                BigDecimal priceDiscount = BigDecimal.ZERO;

                // Check if discount information is present
                if (discountInfo.isPresent()) {
                    Tuple tuple = discountInfo.get();
                    discountPercentage = tuple.get(0, BigDecimal.class);
                    priceDiscount = tuple.get(1, BigDecimal.class);
                }

                // Calculate final price after applying discounts and ensure no negative values
                return standardPrice
                    .subtract(standardPrice.multiply(discountPercentage.divide(new BigDecimal(100), RoundingMode.HALF_EVEN)))
                    .subtract(priceDiscount)
                    .max(BigDecimal.ZERO);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum up all the prices with discounts

        propertyServicePackage.setTotalFee(totalFee);

        // Save the PropertyServicePackage entity
        var savedPropertyServicePackage = propertyServicePackageRepository.save(propertyServicePackage);

        // Fetch PostServices in one go based on request service IDs
        List<PostServiceBasicInfoDTO> postServices = postServiceRepository.getNameAndAvailableUnitsById(request.postServiceIds());

        // Map PostServices info to PostServicePackage entities
        List<PostServicePackage> postServicePackagesEntities = postServices.stream()
            .map(postService -> PostServicePackage.builder() // Use builder instead of MapStruct to reduce code complexity in this case
                .postServicePackageKey(PostServicePackageKey.builder()
                    .postServiceId(postService.postServiceId())
                    .propertyServicePackageId(savedPropertyServicePackage.getPropertyServicePackageId())
                    .build())
                .postService(postServiceMapper.toPostServiceEntity(postService))
                .propertyServicePackage(savedPropertyServicePackage)
                .unitsRemaining(postService.availableUnits())
                .build())
            .toList();

        // Save PostServicePackages in batch
        postServicePackageRepository.saveAll(postServicePackagesEntities);

        return propertyServicePackageMapper.toPropertyServicePackageCreateDTO(propertyServicePackage, postServices);
    }


    @Override
    @Transactional
    public PropertyServicePackageSummaryDTO findPropertyServicePackageWithsPostServices(Long id) {
        String redisKey = "propertyServicePackageSummary:" + id;

        return Optional.ofNullable(redisTemplate.opsForValue().get(redisKey))
            .map(redisData -> {
                try {
                    return objectMapper.readValue(redisData, PropertyServicePackageSummaryDTO.class);
                } catch (JsonProcessingException e) {
                    log.error("Error deserializing Redis data", e);
                    throw new FailedToDeserializingData("Failed to deserializing Redis data");
                }
            })
            .orElseGet(() -> {
                PropertyServicePackageSummaryDTO result = propertyServicePackageRepository.findPropertyServicePackageWithsPostServices(id);
                try {
                    redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(result));
                } catch (JsonProcessingException e) {
                    log.error("Error deserializing Redis data", e);
                    throw new FailedToDeserializingData(e.getMessage());
                }
                return result;
            });
    }

//    @Override
//    @Transactional
//    public PropertyServicePackageSummaryDTO findPropertyServicePackageWithsPostServices(Long id) {
//        return propertyServicePackageRepository.findPropertyServicePackageWithsPostServices(id);
//    }
}

