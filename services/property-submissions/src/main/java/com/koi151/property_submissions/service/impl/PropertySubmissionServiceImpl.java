package com.koi151.property_submissions.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.property_submissions.client.AccountClient;
import com.koi151.property_submissions.client.ListingServicesClient;
import com.koi151.property_submissions.client.PropertyClient;
import com.koi151.property_submissions.customExceptions.*;
import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.kafka.SubmissionConfirmation;
import com.koi151.property_submissions.kafka.SubmissionProducer;
import com.koi151.property_submissions.mapper.PropertySubmissionMapper;
import com.koi151.property_submissions.model.dto.*;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import com.koi151.property_submissions.model.response.CustomerResponse;
import com.koi151.property_submissions.model.response.PurchaseResponse;
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
import java.util.List;
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
        ResponseEntity<ResponseData> propertyPostServiceResponse = serviceResponseValidator.fetchServiceData(
            () -> listingServicesClient.findPropertyPackageServiceById(request.propertyId()), // utilizing a functional interface
            "Listing services",
            "property post service data"
        );

        ResponseEntity<ResponseData> customerResponse = serviceResponseValidator.fetchServiceData(
            () -> accountClient.findAccountDetails(request.accountId()),
            "Account",
            "account data"
        );

        // extract body data
        var customerData = objectMapper.convertValue(Objects.requireNonNull(customerResponse.getBody()).getData(), CustomerResponse.class);
        var purchaseData = objectMapper.convertValue(Objects.requireNonNull(propertyPostServiceResponse.getBody()).getData(), PurchaseResponse.class);

        // validate
        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);

        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
//        entity.setTotalFee();

        propertySubmissionRepository.save(entity);

        // sending a confirmation message about a property submission to a Kafka topic
        submissionProducer.sendSubmissionConfirmation(
            new SubmissionConfirmation( // this will be serialized
                request.referenceCode(),
                BigDecimal.TEN,
                request.paymentMethod(),
                customerData,
                purchaseData
            )
        );

        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }

    @Override
    public AccountWithSubmissionDTO getPropertySubmissionByAccount(Long accountId, Pageable pageable) {
        if (!propertySubmissionRepository.existsByAccountIdAndDeleted(accountId, false))
            throw new AccountNotFoundException("No property submission found from account id: " + accountId);

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
