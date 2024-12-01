package com.koi151.msproperty.mapper;

import com.koi151.msproperty.model.response.ResponseData;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseDataMapper {

    @Mapping(target = "data", source = "propertyPages.content")
    @Mapping(target = "totalPages", source = "propertyPages.totalPages")
    @Mapping(target = "totalItems", source = "propertyPages.totalElements")
    @Mapping(target = "currentPage", source = "page")
    @Mapping(target = "maxPageItems", source = "pageSize")
    ResponseData toResponseData(Page<?> propertyPages, int page, int pageSize);
}
