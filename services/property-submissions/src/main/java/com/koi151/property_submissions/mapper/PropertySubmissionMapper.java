package com.koi151.property_submissions.mapper;

import com.koi151.property_submissions.entity.PropertySubmission;
import com.koi151.property_submissions.model.dto.PropertySubmissionCreateDTO;
import com.koi151.property_submissions.model.dto.PropertySubmissionsDetailsDTO;
import com.koi151.property_submissions.model.dto.PropertySubmissionDetailedDTO;
import com.koi151.property_submissions.model.request.PropertySubmissionCreate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertySubmissionMapper {

    PropertySubmission toPropertySubmissionEntity(PropertySubmissionCreate request);
    @Mapping(target = "status", source = "status.statusName")
    PropertySubmissionCreateDTO toPropertySubmissionCreateDTO(PropertySubmission entity);

    @Mapping(target = "status", source = "status.statusName")
    PropertySubmissionDetailedDTO toPropertySubmissionDetailsDTO(PropertySubmission entity);

    @Mapping(target = "status", source = "status.statusName")
    List<PropertySubmissionDetailedDTO> toPropertySubmissionDetailsDTOs(List<PropertySubmission> entities);

    @Mapping(target = "content", expression = "java(page.getContent())")
    @Mapping(target = "meta.totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "meta.pageSize", expression = "java(page.getPageable().getPageSize())")
    @Mapping(target = "meta.totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "meta.pageNumber", expression = "java(page.getPageable().getPageNumber() + 1)")
    PropertySubmissionsDetailsDTO toPropertySubmissionDTO(Page<PropertySubmissionDetailedDTO> page);


}
