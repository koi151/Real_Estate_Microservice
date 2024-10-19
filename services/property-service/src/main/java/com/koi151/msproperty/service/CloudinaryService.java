package com.koi151.msproperty.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryService {
    String uploadFile(MultipartFile file, String folderName);
    String uploadFiles(List<MultipartFile> files, String folderName);
}
