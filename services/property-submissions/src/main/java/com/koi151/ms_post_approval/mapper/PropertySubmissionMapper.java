package com.koi151.ms_post_approval.mapper;

import com.koi151.ms_post_approval.entity.PropertySubmission;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionCreateDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionDetailsDTO;
import com.koi151.ms_post_approval.model.request.PropertySubmissionCreate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertySubmissionMapper {

    PropertySubmission toPropertySubmissionEntity(PropertySubmissionCreate request);
    PropertySubmissionCreateDTO toPropertySubmissionCreateDTO(PropertySubmission entity);

    @Mapping(target = "status", source = "status.statusName")
    PropertySubmissionDetailsDTO toPropertySubmissionDetailsDTO(PropertySubmission entity);

    @Mapping(target = "content", expression = "java(page.getContent())")
    @Mapping(target = "meta.totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "meta.pageSize", expression = "java(page.getPageable().getPageSize())")
    @Mapping(target = "meta.totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "meta.pageNumber", expression = "java(page.getPageable().getPageNumber() + 1)")
    PropertySubmissionDTO toPropertySubmissionDTO(Page<PropertySubmissionDetailsDTO> page);
}
