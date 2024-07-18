package com.koi151.msproperties.model.dto;

import com.koi151.msproperties.enums.DaysPostedEnum;
import com.koi151.msproperties.enums.PostingPackageEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyPostServiceDTO {

    private String daysPosted;
    private String postingPackage;
    private short priorityPushes;
    private String postingDate;
}
