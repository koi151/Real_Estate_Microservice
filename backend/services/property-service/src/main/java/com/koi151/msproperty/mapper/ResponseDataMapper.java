package com.koi151.msproperty.mapper;

import com.koi151.msproperty.model.response.ResponseData;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResponseDataMapper {

    @Mapping(target = "data", source = "content")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "totalItems", source = "totalElements")
    @Mapping(target = "currentPage", expression = "java(page.getNumber() + 1)") // page.getNumber() start from 0
    @Mapping(target = "maxPageItems", source = "size")
    ResponseData toResponseData(Page<?> page);

}
