package com.example.amago.core.services.upload;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.utils.messages.ExceptionMessage;

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

            String folder = isVideo ? "video" : "image";
            String resourceType = isVideo ? "video" : "image";

            @SuppressWarnings("unchecked")
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", resourceType);

            Map<?, ?> result = cloudinary.uploader()
                    .upload(file.getBytes(), options);

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

    @Override
    public void deleteFile(String publicId, boolean isVideo) {

        try {
            String folder = isVideo ? "video" : "image";
            String resourceType = isVideo ? "video" : "image";

            cloudinary.uploader().destroy(
                    folder + "/" + publicId,
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "invalidate", true));

        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

}
