package com.koi151.listing_services.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PropertyServicePackageCreateDTO {
    private Long propertyId;
    private Long propertyServicePackageId;
    private List<PostServiceBasicInfoDTO> postServices;
    private String packageType;
    private String status;
}
