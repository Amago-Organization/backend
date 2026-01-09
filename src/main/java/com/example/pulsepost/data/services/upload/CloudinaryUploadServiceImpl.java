package com.example.pulsepost.data.services.upload;

import java.io.IOException;

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
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new DomainException(ExceptionMessage.uploadFileError);
        }
    }

}
