package com.koi151.msproperties.service.converter;

import com.koi151.msproperties.entity.PropertyEntity;
import com.koi151.msproperties.entity.PropertyForRentEntity;
import com.koi151.msproperties.entity.PropertyForSaleEntity;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.model.dto.PropertySearchDTO;
import com.koi151.msproperties.model.dto.RoomNameQuantityDTO;
import com.koi151.msproperties.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PropertyConverter {

    @Autowired
    private ModelMapper modelMapper;

    public PropertySearchDTO toPropertySearchResponse(PropertyEntity item) {
        PropertySearchDTO property = modelMapper.map(item, PropertySearchDTO.class);

        String address = String.join(", ",
                item.getAddressEntity().getStreetAddress(),
                item.getAddressEntity().getWard(),
                item.getAddressEntity().getDistrict(),
                item.getAddressEntity().getCity());
        property.setAddress(address);

        if (item.getBalconyDirection() != null) {
            property.setBalconyDirection(item.getBalconyDirection().getDirectionName());
        }

        if (item.getHouseDirection() != null) {
            property.setHouseDirection(item.getHouseDirection().getDirectionName());
        }

        PropertyForRentEntity rentEntity = item.getPropertyForRentEntity();
        PropertyForSaleEntity saleEntity = item.getPropertyForSaleEntity();

        if (rentEntity != null) {
            property.setPrice(rentEntity.getRentalPrice());
            property.setPaymentSchedule(rentEntity.getPaymentSchedule().getScheduleName());
            property.setTerm(rentEntity.getRentTerm());
            property.setType(PropertyTypeEnum.RENT.getPropertyType());
        } else if (saleEntity != null) {
            property.setPrice(saleEntity.getSalePrice());
            property.setTerm(saleEntity.getSaleTerm());
            property.setType(PropertyTypeEnum.SALE.getPropertyType());
        }

        if (item.getRoomEntities() != null) {
            property.setRooms(item.getRoomEntities().stream()
                    .map(room -> RoomNameQuantityDTO.builder()
                            .roomType(room.getRoomType())
                            .quantity(room.getQuantity())
                            .build())
                    .collect(Collectors.toList()));
        }

        property.setStatus(item.getStatus().getStatusName());
        return property;
    }
}
