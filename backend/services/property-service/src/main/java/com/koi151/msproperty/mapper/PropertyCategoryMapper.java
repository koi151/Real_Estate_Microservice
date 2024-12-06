package com.koi151.msproperty.mapper;

import com.koi151.msproperty.entity.PropertyCategory;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryHomeDTO;
import com.koi151.msproperty.model.dto.PropertyCategory.PropertyCategoryTreeDTO;
import com.koi151.msproperty.model.request.propertyCategory.PropertyCategoryCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyCategoryMapper {
    @Mapping(target = "status", source = "status.statusName")
    PropertyCategoryHomeDTO toPropertyCategoryHomeDTO(PropertyCategory entity);

    @Mapping(target = "id", source = "categoryId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "children", ignore = true) // Recursive mapping done manually
    PropertyCategoryTreeDTO toPropertyCategoryTreeDTO(PropertyCategory entity);

    List<PropertyCategoryTreeDTO> toPropertyCategoryTreeDTOList(List<PropertyCategory> entities);

}
