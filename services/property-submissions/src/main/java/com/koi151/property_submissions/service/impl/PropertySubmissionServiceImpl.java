package com.koi151.property_submissions.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.property_submissions.client.AccountClient;
import com.koi151.property_submissions.customExceptions.AccountNotFoundException;
import com.koi151.property_submissions.customExceptions.AccountServiceCommunicationException;
import com.koi151.property_submissions.customExceptions.AccountServiceResponseException;
import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.kafka.SubmissionConfirmation;
import com.koi151.property_submissions.kafka.SubmissionProducer;
import com.koi151.property_submissions.mapper.PropertySubmissionMapper;
import com.koi151.property_submissions.mapper.ResponseDataMapper;
import com.koi151.property_submissions.model.dto.*;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import com.koi151.property_submissions.model.request.PropertySubmissionSearchRequest;
import com.koi151.property_submissions.model.response.ResponseData;
import com.koi151.property_submissions.repository.PropertySubmissionRepository;
import com.koi151.property_submissions.service.PropertySubmissionService;
import com.koi151.property_submissions.validation.PropertySubmissionValidator;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertySubmissionServiceImpl implements PropertySubmissionService {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertySubmissionValidator propertySubmissionValidator;
    private final PropertySubmissionMapper propertySubmissionMapper;
    private final SubmissionProducer submissionProducer;
    private final ObjectMapper objectMapper;

    private final AccountClient accountClient;

    @Override
    public Page<PropertySubmissionDetailedDTO> findAllPropertySubmissions(PropertySubmissionSearchRequest request, Pageable pageable) {
        Page<PropertySubmission> submissionPage = propertySubmissionRepository.findPropertySubmissionsByCriteria(request, pageable);
        List<PropertySubmissionDetailedDTO> DTOs = propertySubmissionMapper.toPropertySubmissionDetailsDTOs(submissionPage.getContent());
        return new PageImpl<>(DTOs, pageable, submissionPage.getTotalElements());
    }

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        var customer = accountClient.

        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);
        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        propertySubmissionRepository.save(entity);

        submissionProducer.sendSubmissionConfirmation(
                new SubmissionConfirmation(
                        request.referenceCode(),
                        100,
                        request.paymentMethod(),
//                        request.pur
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
