package com.koi151.msproperty.model.projection;

import com.koi151.msproperty.entity.Address;
import com.koi151.msproperty.enums.FurnitureEnum;
import com.koi151.msproperty.enums.LegalDocumentEnum;
import com.koi151.msproperty.enums.PropertyTypeEnum;
import com.koi151.msproperty.enums.StatusEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     Address address,
     String imageUrls,
     LegalDocumentEnum legalDocument,
     FurnitureEnum furniture,
     List<RoomSearchProjection> rooms,
     LocalDateTime createdDate
//     PropertyPostServiceProjection propertyPostService
) {}



