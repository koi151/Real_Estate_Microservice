package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PropertySearchDTO {

    private String title;
    private String type;
    private Integer categoryId;
    private Float area;
    private Double rentalPrice;
    private Double salePrice;
    private String description;
    private Integer totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private String status;
    private String availableFrom;
    private String address;
    private List<String> imageUrls;
    private List<RoomNameQuantityDTO> rooms;
}
