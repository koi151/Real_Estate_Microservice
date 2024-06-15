package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.reponse.PropertySearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.Map;


@Component
public class PropertyConverter {

    @Autowired
    private ModelMapper modelMapper;

    public PropertySearchResponse toPropertySearchResponse(PropertyEntity item) {
        PropertySearchResponse building = modelMapper.map(item, PropertySearchResponse.class);
        Map<String, String> statusList = StatusEnum.type();
        building.setAddress(item.getAddressEntity().getStreetAddress() + ", " + item.getAddressEntity().getWard()
                + ", " + item.getAddressEntity().getDistrict() + ", " + item.getAddressEntity().getCity());
        building.setStatus(statusList.get(item.getStatus()));
        return building;
    }

}


