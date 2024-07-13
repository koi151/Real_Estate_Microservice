package com.koi151.ms_post_approval.service.impl;

import com.koi151.ms_post_approval.customExceptions.DuplicatePropertySubmissionException;
import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.mapper.PropertySubmissionMapper;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import com.koi151.ms_post_approval.repository.PropertySubmissionRepository;
import com.koi151.ms_post_approval.service.PropertySubmissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertySubmissionServiceImpl implements PropertySubmissionService {

    private final PropertySubmissionRepository propertySubmissionRepository;
    private final PropertySubmissionMapper propertySubmissionMapper;

    @Override
    @Transactional
    public PropertySubmissionCreateDTO createPropertySubmission(PropertySubmissionCreate request) {
        if (propertySubmissionRepository.existsByPropertyIdAndDeleted(request.getPropertyId(), false))
            throw new DuplicatePropertySubmissionException("Duplicate property submission");

        PropertySubmission entity = propertySubmissionMapper.toPropertySubmissionEntity(request);
        propertySubmissionRepository.save(entity);
        return propertySubmissionMapper.toPropertySubmissionCreateDTO(entity);
    }
}
