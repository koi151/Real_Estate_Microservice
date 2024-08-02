package com.koi151.msproperties.model.projection;

import com.koi151.msproperties.enums.DirectionEnum;
import com.koi151.msproperties.enums.StatusEnum;
import com.koi151.msproperties.model.dto.AddressDTO;
import com.koi151.msproperties.model.dto.RoomNameQuantityDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public interface PropertySearchProjection {
    Long getPropertyId();
    PropertyForRentSearchProjection getPropertyForRent();
    PropertyForSaleSearchProjection getPropertyForSale();
    String getTitle();
    String getType();
    Integer getCategoryId();
    BigDecimal getArea();
    BigDecimal getRentalPrice();
    BigDecimal getSalePrice();
    String getDescription();
    Integer getTotalFloor();
    DirectionEnum getHouseDirection();
    DirectionEnum getBalconyDirection();
    StatusEnum getStatus();
    LocalDate getAvailableFrom();
    AddressDTO getAddress();
    String getImageUrls();
    List<RoomSearchProjection> getRooms();
}
