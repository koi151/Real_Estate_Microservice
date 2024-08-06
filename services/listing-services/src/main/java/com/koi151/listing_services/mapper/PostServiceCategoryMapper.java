package com.koi151.listing_services.mapper;

import com.koi151.listing_services.entity.PostServiceCategory;
import com.koi151.listing_services.model.dto.PostServiceCategoryCreateDTO;
import com.koi151.listing_services.model.request.PostServiceCategoryCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostServiceCategoryMapper {

    PostServiceCategory toPostServiceCategoryEntity(PostServiceCategoryCreateRequest dto);

    PostServiceCategoryCreateDTO toPostServiceCategoryCreateDTO(PostServiceCategory entity);

}
