package com.koi151.msproperty.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertySearchDTO {
    private Long propertyId;
    private String title;
    private String type;
    private Integer categoryId;
    private BigDecimal area;
    private BigDecimal rentalPrice;
    private BigDecimal salePrice;
    private String description;
    private Integer totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private String furniture;
    private String legalDocument;
    private Integer view;
    private String status;
    private String address;
    private List<String> imageUrls;
    private List<RoomNameQuantityDTO> rooms;
    private LocalDateTime createdDate;
}