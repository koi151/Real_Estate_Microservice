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
import com.koi151.ms_post_approval.model.response.PageMeta;
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
    private final ResponseDataMapper responseDataMapper;
    private final ObjectMapper objectMapper;

    private final AccountClient accountClient;

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);
        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        propertySubmissionRepository.save(entity);
        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }

//    @Override
//    public Page<AccountWithSubmissionDTO> getPropertySubmissionByAccount(Long accountId, Pageable pageable) {
//        if (!propertySubmissionRepository.existsByAccountIdAndDeleted(accountId, false))
//            throw new AccountNotFoundException("No property submission found from account id: " + accountId);
//
//        // Fetch Account information using Feign Client
//        ResponseEntity<ResponseData> responseData = accountClient.getAccountNameAndRole(accountId);
//
//        if (responseData.getStatusCode().is2xxSuccessful() && responseData.getBody() != null) {
//            Object accountDataObj = responseData.getBody().getData();
//
//            AccountWithNameAndRoleDTO accountDTO = objectMapper.convertValue(accountDataObj, AccountWithNameAndRoleDTO.class);
//            Page<PropertySubmission> submissionPage =  propertySubmissionRepository.findPropertySubmissionByAccountIdAndDeleted(accountId, false, pageable);
//
//            var test = responseDataMapper.toCustomResponseData(submissionPage);
//
//            // Map to PropertySubmissionDTO - parameter for .toAccountWithSubmissionDTO method
//            Page<PropertySubmissionDTO> submissionDTOPage = submissionPage.map(propertySubmissionMapper::toPropertySubmissionDTO);
//            List<PropertySubmissionDTO> submissionDTOList = submissionDTOPage.getContent();
//
//            var result = AccountWithSubmissionDTO.builder()
////                    .propertySubmissionPages(submissionDTOList)
////                    .accountDTO(accountDTO)
//                    .build();
//
//            return new PageImpl<>(List.of(result), pageable, submissionDTOPage.getTotalElements());
//        } else {
//            throw new AccountServiceResponseException("Failed to fetch account information from account service");
//        }
//    }

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
            Page<PropertySubmissionDetailsDTO> submissionDTOPage = submissionPage
                    .map(propertySubmissionMapper::toPropertySubmissionDetailsDTO);

            PropertySubmissionDTO propertySubmissionDTO = propertySubmissionMapper.toPropertySubmissionDTO(submissionDTOPage);

            return AccountWithSubmissionDTO.builder()
                    .accountWithNameAndRoleDTO(accountDTO)
                    .propertySubmissionDTO(propertySubmissionDTO)
                    .build();

        } else {
            throw new AccountServiceResponseException("Failed to fetch account information from account service");
        }
    }
}
