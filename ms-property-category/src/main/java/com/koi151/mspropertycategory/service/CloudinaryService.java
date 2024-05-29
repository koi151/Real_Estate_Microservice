package com.koi151.mspropertycategory.service;

import com.cloudinary.Cloudinary;
import com.koi151.mspropertycategory.service.imp.CloudinaryServiceImp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
}
