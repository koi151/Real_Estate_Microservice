package com.koi151.ms_post_approval.service.impl;

import com.koi151.ms_post_approval.customExceptions.AccountNotFoundException;
import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.mapper.PropertySubmissionMapper;
import com.koi151.ms_post_approval.model.dto.AccountSubmissionDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.repository.PropertySubmissionRepository;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import com.koi151.ms_post_approval.validation.PropertySubmissionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertySubmissionServiceImpl implements PropertySubmissionService {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertySubmissionValidator propertySubmissionValidator;
    private final PropertySubmissionMapper propertySubmissionMapper;

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        propertySubmissionValidator.validatePropertySubmissionCreateRequest(request);
        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        propertySubmissionRepository.save(entity);
        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }

    @Override
    public Page<AccountSubmissionDTO> getPropertySubmissionByAccount(Long accountId, Pageable pageable) {
        if (!propertySubmissionRepository.existsByAccountIdAndDeleted(accountId, false))
            throw new AccountNotFoundException("No property submission found from account id: " + accountId);

        Page<PropertySubmission> submissionPages =  propertySubmissionRepository.findPropertySubmissionByAccountIdAndDeleted(accountId, false, pageable);
        return submissionPages.map(propertySubmissionMapper::toAccountSubmissionDTO);
    }
}
