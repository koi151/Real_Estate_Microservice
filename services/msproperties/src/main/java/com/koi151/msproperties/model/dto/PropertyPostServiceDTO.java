package com.koi151.msproperties.model.dto;
import lombok.Data;

@Data
public class PropertyPostServiceDTO {

    private String daysPosted;
    private String postingPackage;
    private short priorityPushes;
    private String postingDate;
}
