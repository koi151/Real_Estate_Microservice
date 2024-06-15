package com.koi151.msproperties.model.reponse;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.PaymentScheduleEnum;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.RoomDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PropertySearchResponse {

    private String title;
    private PropertyTypeEnum type;
    private PaymentScheduleEnum paymentSchedule;
    private Integer categoryId;
    private Float area;
    private PropertyTypeEnum propertyType;
    private Float price;
    private String description;
    private Integer totalFloor;
    private DirectionEnum houseDirection;
    private DirectionEnum balconyDirection;
    private String status;
    private String availableFrom;
    private String term;
    private String city;
    private String district;
    private String ward;
    private String address;
    private List<RoomDTO> rooms;
}
