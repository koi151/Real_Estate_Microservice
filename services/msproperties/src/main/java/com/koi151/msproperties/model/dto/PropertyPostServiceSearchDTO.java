package com.koi151.msproperties.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PropertyPostServiceSearchDTO {
    private String postingPackage;
    private LocalDateTime postingDate;
}
