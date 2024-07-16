package com.koi151.ms_post_approval.mapper;

import com.koi151.ms_post_approval.model.dto.AccountWithSubmissionDTO;
import com.koi151.ms_post_approval.model.dto.PropertySubmissionDetailsDTO;
import com.koi151.ms_post_approval.model.response.PageMeta;
import com.koi151.ms_post_approval.model.response.ResponseData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseDataMapper {
//    @Mapping(target = "data", source = "propertyPages.content")
//    @Mapping(target = "totalPages", source = "propertyPages.totalPages")
//    @Mapping(target = "totalItems", source = "propertyPages.totalElements")
//    @Mapping(target = "currentPage", source = "page")
//    @Mapping(target = "maxPageItems", source = "limit")
//    ResponseData toResponseData(Page<?> propertyPages, int page, int limit);

}