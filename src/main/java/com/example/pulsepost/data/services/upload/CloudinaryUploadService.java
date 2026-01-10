package com.example.pulsepost.data.services.upload;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryUploadService {

    public String uploadFile(MultipartFile file, String publicId);
    public void deleteFile(String publicId, boolean isVideo);

}