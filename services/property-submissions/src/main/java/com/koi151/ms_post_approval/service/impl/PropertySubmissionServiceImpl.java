package com.koi151.ms_post_approval.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koi151.ms_post_approval.client.AccountClient;
import com.koi151.ms_post_approval.customExceptions.AccountNotFoundException;
import com.koi151.ms_post_approval.customExceptions.AccountServiceResponseException;
import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.mapper.PropertySubmissionMapper;
import com.koi151.ms_post_approval.mapper.ResponseDataMapper;
import com.koi151.ms_post_approval.model.dto.*;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.model.request.PropertySubmissionSearchRequest;
import com.koi151.ms_post_approval.model.response.ResponseData;
import com.koi151.ms_post_approval.repository.PropertySubmissionRepository;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import com.koi151.ms_post_approval.validation.PropertySubmissionValidator;
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
        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);
        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        propertySubmissionRepository.save(entity);
        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }

    @Override
    public AccountWithSubmissionDTO getPropertySubmissionByAccount(Long accountId, Pageable pageable) {
        if (!propertySubmissionRepository.existsByAccountIdAndDeleted(accountId, false))
            throw new AccountNotFoundException("No property submission found from account id: " + accountId);

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
    }
}
