package com.example.pulsepost.data.services.upload;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pulsepost.domain.exceptions.DomainException;
import com.example.pulsepost.presentation.messages.ExceptionMessage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CloudinaryUploadServiceImpl implements CloudinaryUploadService {

    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String publicId) {
        try {

            String contentType = file.getContentType();

            if (contentType == null) {
                throw new DomainException(ExceptionMessage.invalidFileType);
            }

            boolean isPng = contentType.equals("image/png");
            boolean isJpg = contentType.equals("image/jpeg");
            boolean isMp4 = contentType.equals("video/mp4");

            if (!isPng && !isJpg && !isMp4) {
                throw new DomainException(ExceptionMessage.invalidFileType);
            }

            boolean isVideo = isMp4;
            String typePrefix = isVideo ? "video" : "image";

            @SuppressWarnings("unchecked")
            Map<String, Object> options = ObjectUtils.asMap(
                    "public_id", typePrefix + "/" + publicId,
                    "overwrite", true,
                    "resource_type", isVideo ? "video" : "image");

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), options);

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

    @Override
    public void deleteFile(String publicId, boolean isVideo) {
        try {
            String typePrefix = isVideo ? "video" : "image";
            cloudinary.uploader().destroy(
                    typePrefix + "/" + publicId,
                    ObjectUtils.asMap("resource_type", isVideo ? "video" : "image"));
        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

}
