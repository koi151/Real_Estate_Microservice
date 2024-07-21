package com.koi151.msproperties.mapper;

import com.koi151.msproperties.model.reponse.ResponseData;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseDataMapper {

    @Mapping(target = "data", source = "propertyPages.content")
    @Mapping(target = "totalPages", source = "propertyPages.totalPages")
    @Mapping(target = "totalItems", source = "propertyPages.totalElements")
    @Mapping(target = "currentPage", source = "page")
    @Mapping(target = "maxPageItems", source = "limit")
    ResponseData toResponseData(Page<?> propertyPages, int page, int limit);
}
