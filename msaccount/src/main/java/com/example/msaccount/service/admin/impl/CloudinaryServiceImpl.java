package com.example.msaccount.service.admin.impl;

import com.cloudinary.Cloudinary;
import com.example.msaccount.service.admin.CloudinaryService;
import com.example.msaccount.customExceptions.EmptyFileException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try{
            // upload file option
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName); // name of folder in Cloudinary

            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty())
                throw new EmptyFileException("Empty file");

            // upload file to CLoudinary and get file info
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadedFile.get("public_id");
            // create safe URL for file access
            return cloudinary.url().secure(true).generate(publicId);

        }catch (IOException e){
            System.out.println("Error occurred in Cloudinary service: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String uploadFiles(List<MultipartFile> files, String folderName) {
        return files.stream()
                .map(file -> uploadFile(file, folderName))
                .filter(url -> url != null && !url.isEmpty())
                .collect(Collectors.joining(","));
    }
}
