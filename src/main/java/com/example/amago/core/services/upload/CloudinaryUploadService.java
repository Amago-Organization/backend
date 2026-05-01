package com.example.amago.core.services.upload;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryUploadService {

    public String uploadFile(MultipartFile file, String publicId);
    public void deleteFile(String publicId, boolean isVideo);

}