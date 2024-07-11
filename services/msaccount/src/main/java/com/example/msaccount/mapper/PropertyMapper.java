package com.example.msaccount.mapper;

import com.example.msaccount.model.dto.PropertyDTO;
import com.example.msaccount.utils.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {StringUtil.class})
public interface PropertyMapper {

    PropertyMapper INSTANCE = Mappers.getMapper( PropertyMapper.class);

    public List<PropertyDTO> toPropertyDTO(List<?> li);


}
