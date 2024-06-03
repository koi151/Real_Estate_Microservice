package com.koi151.msproperties.service;

import com.cloudinary.Cloudinary;
import com.koi151.msproperties.service.imp.CloudinaryServiceImp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CloudinaryService implements CloudinaryServiceImp {
    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try{
            // upload file option
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName); // name of folder in Cloudinary

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
                .filter(url -> url != null && !url.isEmpty()) // Filtering out null or empty URLs
                .collect(Collectors.joining(",")); // Collecting URLs into a comma-separated String
    }
}
