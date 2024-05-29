package com.koi151.msproperties.entity.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PropertyRequest {

    private String title;
    private Integer categoryId;
    private Float area;
    private String description;

    private Integer totalFloor;
    private String houseDirection;
    private String balconyDirection;
    private Float price;

    private String availableFrom;
    private String status;
    private MultipartFile images;
}
