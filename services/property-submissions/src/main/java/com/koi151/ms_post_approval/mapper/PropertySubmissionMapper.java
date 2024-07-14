package com.koi151.ms_post_approval.mapper;

import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.model.dto.AccountSubmissionDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertySubmissionMapper {

    PropertySubmission toPropertySubmissionEntity(PropertySubmissionCreate request);
    PropertySubmissionCreateDTO toPropertySubmissionCreateDTO(PropertySubmission entity);
    @Mapping(target = "status", source = "status.statusName")
    AccountSubmissionDTO toAccountSubmissionDTO(PropertySubmission entity);
}
