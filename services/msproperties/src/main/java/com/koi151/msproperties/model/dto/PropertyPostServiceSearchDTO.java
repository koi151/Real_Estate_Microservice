package com.koi151.msproperties.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PropertyPostServiceSearchDTO {
    private String postingPackage;
//    private LocalDateTime postingDate;
}
