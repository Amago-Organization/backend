package com.example.pulsepost.domain.dtos.post;

import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.domain.enums.PostTypeEnum;

public record PostUpdateDto(String title, String description, PostTypeEnum postType, MultipartFile file) {
}
