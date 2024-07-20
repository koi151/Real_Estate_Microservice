package com.koi151.msproperties.mapper;

import com.koi151.msproperties.entity.PropertyPostService;
import com.koi151.msproperties.model.dto.PropertyPostServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyPostServiceMapper {

    @Mapping(target = "daysPosted", source = "daysPosted.day")
    @Mapping(target = "postingPackage", source = "postingPackage.packageName")
    PropertyPostServiceDTO toPropertyPostServiceDTO(PropertyPostService entity);
}
