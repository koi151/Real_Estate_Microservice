package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.enums.PropertyTypeEnum;
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

        String addressBuilder = item.getAddressEntity().getStreetAddress() +
                ", " + item.getAddressEntity().getWard() +
                ", " + item.getAddressEntity().getDistrict() +
                ", " + item.getAddressEntity().getCity();
        building.setAddress(addressBuilder);

        if (item.getPropertyForRentEntity() != null) {
            building.setPrice(item.getPropertyForRentEntity().getRentalPrice());
            building.setType(PropertyTypeEnum.RENT);
        } else {
            building.setPrice(item.getPropertyForSaleEntity().getSalePrice());
            building.setType(PropertyTypeEnum.SALE);

        }

//        Map<StatusEnum, String> statusMap = new EnumMap<>(StatusEnum.class);
//        for (StatusEnum status : StatusEnum.values()) {
//            statusMap.put(status, status.getStatusName());
//        }
//
//        building.setStatus(statusMap.get(item.getStatus()));

        building.setStatus(item.getStatus().name());
        building.setPaymentSchedule(item.getPropertyForRentEntity().getPaymentSchedule());

        return building;
    }
}


