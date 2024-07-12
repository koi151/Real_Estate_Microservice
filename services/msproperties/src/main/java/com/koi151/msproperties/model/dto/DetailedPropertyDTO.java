package com.koi151.msproperties.model.dto;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedPropertyDTO {

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
