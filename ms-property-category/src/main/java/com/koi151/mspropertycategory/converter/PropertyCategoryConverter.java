package com.koi151.mspropertycategory.converter;

import com.koi151.mspropertycategory.entity.PropertyCategoryEntity;
import com.koi151.mspropertycategory.model.response.PropertyCategorySearchResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyCategoryConverter {

    @Autowired
    private ModelMapper modelMapper;

    public PropertyCategorySearchResponse toPropertyCategorySearchResponse(PropertyCategoryEntity entity) {
        return  modelMapper.map(entity, PropertyCategorySearchResponse.class);
    }

}
