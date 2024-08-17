package com.koi151.listing_services.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyServicePackageSummaryDTO {
    Long propertyPostPackageId;
    String packageType;
    List<PostServiceBasicInfoDTO> postServiceBasicInfoDTOs;
}
