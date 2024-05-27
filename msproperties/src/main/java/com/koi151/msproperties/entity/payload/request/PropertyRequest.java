package com.koi151.msproperties.entity.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PropertyRequest {

    private String title;
    private int categoryId;
    private float area;
    private String description;
    private String images;
    private int totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private String availableFrom;

}
