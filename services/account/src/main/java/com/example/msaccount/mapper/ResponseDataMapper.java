package com.example.msaccount.mapper;

import com.example.msaccount.model.response.PageMeta;
import com.example.msaccount.model.response.ResponseData;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ResponseDataMapper {

    @Mapping(target = "data", source = "propertyPages.content")
    @Mapping(target = "meta", source = "propertyPages", qualifiedByName = "pageToPageMeta")
    ResponseData toResponseData(Page<?> propertyPages, @Context PaginationContext paginationContext);

    @Named("pageToPageMeta")
    default PageMeta pageToPageMeta(Page<?> page, @Context PaginationContext paginationContext) {
        return PageMeta.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageNumber(paginationContext.pageNumber())
                .pageSize(paginationContext.pageSize())
                .build();
    }
}
