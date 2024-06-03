package com.koi151.msproperties.service.imp;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryServiceImp {
    String uploadFile(MultipartFile file, String folderName);
    String uploadFiles(List<MultipartFile> files, String folderName);
}
