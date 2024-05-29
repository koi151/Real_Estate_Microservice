package com.koi151.mspropertycategory.entity.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageModel {
    private MultipartFile file;
    private String name;
    private String url;
}
