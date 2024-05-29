package com.koi151.mspropertycategory.service.imp;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryServiceImp {
    String uploadFile(MultipartFile file, String folderName);
}
