package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.PropertyForRentEntity;
import com.koi151.msproperties.entity.PropertyForSaleEntity;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.reponse.PropertySearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.Map;
import java.util.Optional;


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

        // Use Optional or ternary operator for price, paymentSchedule, term and type
        building.setPrice(Optional.ofNullable(item.getPropertyForRentEntity())
                .map(PropertyForRentEntity::getRentalPrice)
                .orElseGet(() -> Optional.ofNullable(item.getPropertyForSaleEntity())
                        .map(PropertyForSaleEntity::getSalePrice)
                        .orElse(null)));

        building.setPaymentSchedule(Optional.ofNullable(item.getPropertyForRentEntity())
                .map(PropertyForRentEntity::getPaymentSchedule)
                .orElse(null));

        building.setTerm(Optional.ofNullable(item.getPropertyForRentEntity())
                .map(PropertyForRentEntity::getRentTerm)
                .orElseGet(() -> Optional.ofNullable(item.getPropertyForSaleEntity())
                        .map(PropertyForSaleEntity::getSaleTerm)
                        .orElse(null)));

        building.setType(item.getPropertyForRentEntity() != null ? PropertyTypeEnum.RENT :
                item.getPropertyForSaleEntity() != null ? PropertyTypeEnum.SALE : null);

        building.setStatus(item.getStatus().name());
        return building;
    }

//    public PropertySearchResponse toPropertySearchResponse(PropertyEntity item) {
//        PropertySearchResponse building = modelMapper.map(item, PropertySearchResponse.class);
//
//        String addressBuilder = item.getAddressEntity().getStreetAddress() +
//                ", " + item.getAddressEntity().getWard() +
//                ", " + item.getAddressEntity().getDistrict() +
//                ", " + item.getAddressEntity().getCity();
//        building.setAddress(addressBuilder);
//
//
//        if (item.getPropertyForRentEntity() != null) {
//            building.setPrice(item.getPropertyForRentEntity().getRentalPrice());
//            building.setPaymentSchedule(item.getPropertyForRentEntity().getPaymentSchedule());
//            building.setType(PropertyTypeEnum.RENT);
//            building.setTerm(item.getPropertyForRentEntity().getRentTerm());
//        } else if (item.getPropertyForSaleEntity() != null) {
//            building.setPrice(item.getPropertyForSaleEntity().getSalePrice());
//            building.setType(PropertyTypeEnum.SALE);
//            building.setTerm(item.getPropertyForSaleEntity().getSaleTerm());
//        }
//
//        building.setStatus(item.getStatus().name());
//        return building;
//    }

}


