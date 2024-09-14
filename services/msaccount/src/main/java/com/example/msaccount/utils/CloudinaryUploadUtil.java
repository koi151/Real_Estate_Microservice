package com.example.msaccount.utils;

import com.example.msaccount.customExceptions.CloudinaryUploadFailedException;
import com.example.msaccount.service.admin.impl.CloudinaryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class CloudinaryUploadUtil {

    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public String avatarCloudinaryUpdate(MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            String avatarUploadedUrl = cloudinaryServiceImpl.uploadFile(avatar, "real_estate_account");

            if (!StringUtil.checkString(avatarUploadedUrl))
                throw new CloudinaryUploadFailedException("Failed to upload images to Cloudinary");

            return avatarUploadedUrl;
        }
        return null;
    }
}
