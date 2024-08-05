package com.koi151.msproperties.model.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.koi151.msproperties.entity.Address;
import com.koi151.msproperties.enums.PropertyTypeEnum;
import com.koi151.msproperties.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PropertySearchProjection {
    private Long propertyId;
    private String title;
    private PropertyTypeEnum type;
    private Integer categoryId;
    private BigDecimal area;
    private BigDecimal rentalPrice;
    private BigDecimal salePrice;
    private String description;
    private Short totalFloor;
    private StatusEnum status;
    private LocalDate availableFrom;
    private Address address;
    private String imageUrls;
    private List<RoomSearchProjection> rooms;
    private PropertyPostServiceProjection propertyPostService;
}



