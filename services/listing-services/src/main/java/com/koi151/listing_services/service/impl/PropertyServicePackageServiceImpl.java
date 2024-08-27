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
import com.koi151.listing_services.model.request.PropertyServicePackageSearchRequest;
import com.koi151.listing_services.repository.*;
import com.koi151.listing_services.service.PropertyServicePackageService;
import com.koi151.listing_services.validator.PostServiceValidate;
import com.koi151.listing_services.validator.PropertyServicePackageSearchValidate;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
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
    private final ObjectMapper objectMapper;

    private final PostServiceValidate postServiceValidate;
    private final PropertyServicePackageSearchValidate propertyServicePackageSearchValidate;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public PropertyServicePackageCreateDTO createPropertyServicePackage(PropertyServicePackageCreateRequest request) {
        // Validate the request
        postServiceValidate.postServiceCreateValidate(request);

        // Create and save PropertyServicePackage entity from request
        PropertyServicePackage propertyServicePackage = propertyServicePackageMapper.toPropertyServicePackageEntity(request);
        var savedPropertyServicePackage = propertyServicePackageRepository.save(propertyServicePackage);

        // Fetch PostServices with NAME and ID in one go based on request service IDs
        List<PostServiceBasicInfoDTO> postServices = postServiceRepository.getNameAndAvailableUnitsById(request.postServiceIds());

        // Map PostServices info to PostServicePackage entities
        List<PostServicePackage> postServicePackagesEntities = postServices.stream()
            .map(postService -> {
                Optional<Tuple> discountInfo = promotionRepository.findPromotionInfoByPostServiceIdAndPackageType(postService.postServiceId(), request.packageType());

                // Initialize discount values
                BigDecimal discountPercentage = BigDecimal.ZERO;
                BigDecimal priceDiscount = BigDecimal.ZERO;

                if (discountInfo.isPresent()) {
                    Tuple tuple = discountInfo.get();
                    discountPercentage = tuple.get(0, BigDecimal.class);
                    priceDiscount = tuple.get(1, BigDecimal.class);
                }

                return PostServicePackage.builder() // Use builder instead of MapStruct to reduce code complexity in this case
                    .postServicePackageKey(PostServicePackageKey.builder()
                        .postServiceId(postService.postServiceId())
                        .propertyServicePackageId(savedPropertyServicePackage.getPropertyServicePackageId())
                        .build())
                    .postService(postServiceMapper.toPostServiceEntity(postService))
                    .propertyServicePackage(savedPropertyServicePackage)
                    .unitsRemaining(postService.availableUnits())
                    .priceAtCreation(postServicePricingRepository
                        .findPriceByPostServiceIdAndPackageType(postService.postServiceId(), request.packageType())
                        .orElseThrow(() -> new EntityNotFoundCustomException(
                            "Price not found for post service ID: " + postService.postServiceId() + " and package type: " + request.packageType()
                        )))
                .priceDiscountAtCreation(priceDiscount)
                .discountPercentageAtCreation(discountPercentage)
                .build();
            })
            .toList();

        // Save PostServicePackages in batch
        postServicePackageRepository.saveAll(postServicePackagesEntities);

        BigDecimal totalFee = postServicePackagesEntities.stream()
            .map(postServicePackage -> {
                BigDecimal standardPrice = postServicePackage.getPriceAtCreation();
                BigDecimal discountPercentage = postServicePackage.getDiscountPercentageAtCreation();

                // Calculate final price after applying discounts and ensure no negative values
                return standardPrice
                    .subtract(standardPrice.multiply(discountPercentage.divide(new BigDecimal(100), RoundingMode.HALF_EVEN)))
                    .subtract(postServicePackage.getPriceDiscountAtCreation())
                    .max(BigDecimal.ZERO);
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

        return propertyServicePackageMapper.toPropertyServicePackageCreateDTO(propertyServicePackage, postServices, totalFee);
    }


    @Override
    @Transactional
    public PropertyServicePackageSummaryDTO findPropertyServicePackageByCriteria(Map<String, String> params) {
        propertyServicePackageSearchValidate.propertyServicePackageSearchValidate(params);
        String redisKey = "propertyServicePackageSummary:" + params.get("packageId") + params.get("propertyId");

        return Optional.ofNullable(redisTemplate.opsForValue().get(redisKey))
            .map(redisData -> { // data available in redis
                try {
                    return objectMapper.readValue(redisData, PropertyServicePackageSummaryDTO.class);
                } catch (JsonProcessingException e) {
                    log.error("Error deserializing Redis data", e);
                    throw new FailedToDeserializingData("Failed to deserializing Redis data");
                }
            })
            .orElseGet(() -> { // in case of redis not saved data
                PropertyServicePackageSummaryDTO result = propertyServicePackageRepository.findPropertyServicePackageByCriteria(params);
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


