package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.entity.AddressEntity;
import com.koi151.msproperties.entity.PropertyForRentEntity;
import com.koi151.msproperties.entity.PropertyForSaleEntity;
import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullPropertyDTO {

    private String title;
    private Integer categoryId;
    private Float area;
    private String description;
    private Integer totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private String status;
    private String availableFrom;
    private List<String> imageUrls;
    private List<RoomNameQuantityDTO> rooms;
    private String address;
    private PropertyForSaleCreateDTO propertyForSale;
    private PropertyForRentCreateDTO propertyForRent;
}
