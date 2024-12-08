package com.koi151.msproperty.model.dto;

import lombok.*;
import java.util.List;

@Builder
@Data
public class DetailedPropertyDTO {
    private String title;
    private Integer categoryId;
    private Float area;
    private String description;
    private Integer totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private String status;
    private String legalDocument;
    private String furniture;
    private List<String> imageUrls;
    private List<RoomNameQuantityDTO> rooms;
    private String address;
    private PropertyPostServiceDTO propertyPostService;
    private PropertyForSaleCreateDTO propertyForSale;
    private PropertyForRentCreateDTO propertyForRent;
}
