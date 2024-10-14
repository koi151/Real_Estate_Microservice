package com.koi151.msproperties.model.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyPostServiceDTO {
    private String daysPosted;
    private String postingPackage;
    private short priorityPushes;
    private LocalDateTime postingDate;
}
