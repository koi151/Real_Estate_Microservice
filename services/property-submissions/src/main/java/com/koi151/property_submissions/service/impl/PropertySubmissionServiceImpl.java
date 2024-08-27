package com.koi151.property_submissions.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.property_submissions.client.AccountClient;
import com.koi151.property_submissions.client.ListingServicesClient;
import com.koi151.property_submissions.client.PaymentClient;
import com.koi151.property_submissions.client.PropertyClient;
import com.koi151.property_submissions.customExceptions.*;
import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.kafka.SubmissionConfirmation;
import com.koi151.property_submissions.kafka.SubmissionProducer;
import com.koi151.property_submissions.mapper.PropertySubmissionMapper;
import com.koi151.property_submissions.model.dto.*;
import com.koi151.property_submissions.model.request.PaymentCreateRequest;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import com.koi151.property_submissions.model.response.CustomerResponse;
import com.koi151.property_submissions.model.response.PropertyServicePackageResponse;
import com.koi151.property_submissions.model.response.ResponseData;
import com.koi151.property_submissions.repository.PropertySubmissionRepository;
import com.koi151.property_submissions.service.PropertySubmissionService;
import com.koi151.property_submissions.validator.PropertySubmissionValidator;
import com.koi151.property_submissions.validator.ServiceResponseValidator;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertySubmissionServiceImpl implements PropertySubmissionService {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertySubmissionValidator propertySubmissionValidator;
    private final PropertySubmissionMapper propertySubmissionMapper;
    private final SubmissionProducer submissionProducer;
    private final ObjectMapper objectMapper;

    private final AccountClient accountClient;
    private final ListingServicesClient listingServicesClient;
    private final PaymentClient paymentClient;

    // validator
    private final ServiceResponseValidator serviceResponseValidator;

    @Override
    public Page<PropertySubmissionDetailedDTO> findAllPropertySubmissions(PropertySubmissionSearchRequest request, Pageable pageable) {
        Page<PropertySubmission> submissionPage = propertySubmissionRepository.findPropertySubmissionsByCriteria(request, pageable);
        List<PropertySubmissionDetailedDTO> DTOs = propertySubmissionMapper.toPropertySubmissionDetailsDTOs(submissionPage.getContent());
        return new PageImpl<>(DTOs, pageable, submissionPage.getTotalElements());
    }

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        // Convert the request object to a map of query parameters
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("propertyId", String.valueOf(request.propertyId()));

        ResponseEntity<ResponseData> propertyPostServiceResponse = serviceResponseValidator.fetchServiceData(
            () -> listingServicesClient.findPropertyServicePackageByCriteria(queryParams), // utilizing a functional interface
            "Listing services",
            "property post service data"
        );

        ResponseEntity<ResponseData> customerResponse = serviceResponseValidator.fetchServiceData(
            () -> accountClient.findAccountDetails(request.accountId()),
            "Account",
            "account data"
        );

        // extract body data
        var propertyPackageData = objectMapper.convertValue(Objects.requireNonNull(propertyPostServiceResponse.getBody()).getData(), PropertyServicePackageResponse.class);
        if (propertyPackageData == null)
            throw new EntityNotFoundException("Property service package not found with id: " + request.propertyId());

        var accountData = objectMapper.convertValue(Objects.requireNonNull(customerResponse.getBody()).getData(), CustomerResponse.class);

        // validate
        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);

        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        entity.setTotalFee(propertyPackageData.totalFee());

        propertySubmissionRepository.save(entity);

        // payment request
        PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
            .propertyId(request.propertyId())
            .totalFee(propertyPackageData.totalFee())
            .orderInfo("Payment information of services used for property post with id:" + request.propertyId())
            .bankCode("VCB")
            .transactionNo("273423774268")
            .payDate(LocalDateTime.now())
            .status("pending")
            .build();

        paymentClient.createPayment(paymentRequest);


        // sending a confirmation message about a property submission to a Kafka topic
        submissionProducer.sendSubmissionConfirmation(
            new SubmissionConfirmation( // this will be serialized
                request.referenceCode(),
                BigDecimal.TEN,
                request.paymentMethod(),
                accountData,
                propertyPackageData
            )
        );

        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }

    @Override
    public AccountWithSubmissionDTO getPropertySubmissionByAccount(Long accountId, Pageable pageable) {
        if (!propertySubmissionRepository.existsByAccountIdAndDeleted(accountId, false))
            throw new EntityNotFoundException("No property submission found with account id: " + accountId);

        try {
            ResponseEntity<ResponseData> responseData = accountClient.findAccountNameAndRoleById(accountId);

            if (responseData.getStatusCode().is2xxSuccessful() && responseData.getBody() != null) {
                Object accountDataObj = responseData.getBody().getData();

                AccountWithNameAndRoleDTO accountDTO = objectMapper.convertValue(accountDataObj, AccountWithNameAndRoleDTO.class);

                Page<PropertySubmission> submissionPage = propertySubmissionRepository
                        .findPropertySubmissionByAccountIdAndDeleted(accountId, false, pageable);

                // Map to PropertySubmissionDTO and create AccountWithSubmissionDTO
                Page<PropertySubmissionDetailedDTO> submissionDTOPage = submissionPage
                        .map(propertySubmissionMapper::toPropertySubmissionDetailsDTO);

                PropertySubmissionsDetailsDTO propertySubmissionsDetailsDTO = propertySubmissionMapper.toPropertySubmissionDTO(submissionDTOPage);

                return AccountWithSubmissionDTO.builder()
                        .accountWithNameAndRoleDTO(accountDTO)
                        .propertySubmissionsDetailsDTO(propertySubmissionsDetailsDTO)
                        .build();

            } else {
                throw new AccountServiceResponseException("Failed to fetch account information from account service");
            }
        } catch (FeignException ex) {
            System.out.println("Error fetching account information: {}" + ex.getMessage());
            throw new AccountServiceCommunicationException("Error communicating with account service");
        }
    }
}


// error handler for payment
// check condition payment