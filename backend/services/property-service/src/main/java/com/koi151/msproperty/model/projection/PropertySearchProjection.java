package com.koi151.msproperty.model.projection;

import com.koi151.msproperty.entity.Address;
import com.koi151.msproperty.enums.PropertyTypeEnum;
import com.koi151.msproperty.enums.StatusEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record PropertySearchProjection (
     Long propertyId,
     String title,
     PropertyTypeEnum type,
     Integer categoryId,
     BigDecimal area,
     BigDecimal rentalPrice,
     BigDecimal salePrice,
     Integer view,
     String description,
     Short totalFloor,
     StatusEnum status,
     LocalDate availableFrom,
     Address address,
     String imageUrls,
     List<RoomSearchProjection> rooms
//     PropertyPostServiceProjection propertyPostService
) {}



