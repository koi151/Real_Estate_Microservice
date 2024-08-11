package com.koi151.listing_services.model.dto;

import com.koi151.listing_services.entity.PostService;
import lombok.Data;

import java.util.List;

@Data
public class PropertyServicePackageCreateDTO {
    private Long propertyServiceId;
    private PostService postService;
    private List<PostServicePackageDTO> postServicePackages;
    private String packageType;
    private String status;
}
