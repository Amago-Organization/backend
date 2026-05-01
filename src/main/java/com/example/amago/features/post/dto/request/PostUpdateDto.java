package com.example.amago.features.post.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.amago.features.post.enums.PostTypeEnum;



public record PostUpdateDto(String title, String description, PostTypeEnum postType, MultipartFile file) {
}
