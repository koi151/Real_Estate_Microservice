package com.koi151.property_submissions.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.property_submissions.client.AccountClient;
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
import com.koi151.property_submissions.validation.PropertySubmissionValidator;
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
    private final PropertyClient propertyClient;

    @Override
    public Page<PropertySubmissionDetailedDTO> findAllPropertySubmissions(PropertySubmissionSearchRequest request, Pageable pageable) {
        Page<PropertySubmission> submissionPage = propertySubmissionRepository.findPropertySubmissionsByCriteria(request, pageable);
        List<PropertySubmissionDetailedDTO> DTOs = propertySubmissionMapper.toPropertySubmissionDetailsDTOs(submissionPage.getContent());
        return new PageImpl<>(DTOs, pageable, submissionPage.getTotalElements());
    }

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        try {
            var customerResponse = accountClient.findAccountDetails(request.accountId());
            var propertyPostServiceResponse = propertyClient.findPostServicesById(request.propertyId());

            if (!propertyPostServiceResponse.getStatusCode().is2xxSuccessful() && propertyPostServiceResponse.getBody() == null)
                throw new PropertyServiceResponseException("Failed to fetch property post service data from Property service");
            if (!customerResponse.getStatusCode().is2xxSuccessful() && customerResponse.getBody() == null)
                throw new AccountServiceResponseException("Failed to fetch account data from Account service");

            var customerData = objectMapper.convertValue(Objects.requireNonNull(Objects.requireNonNull(customerResponse.getBody()).getData()), CustomerResponse.class);
            var purchaseData = objectMapper.convertValue(Objects.requireNonNull(propertyPostServiceResponse.getBody()).getData(), PurchaseResponse.class);

            propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);
            PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
            propertySubmissionRepository.save(entity);

            submissionProducer.sendSubmissionConfirmation(
                    new SubmissionConfirmation(
                            request.referenceCode(),
                            BigDecimal.TEN,
                            request.paymentMethod(),
                            customerData,
                            purchaseData
                    )
            );

            return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);

        } catch (FeignException ex) {
            log.error("Error occurred while fetching data in other service: {}", ex.getMessage());
            throw new ServiceCommunicationException("Error communicating with other service");
        }
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
