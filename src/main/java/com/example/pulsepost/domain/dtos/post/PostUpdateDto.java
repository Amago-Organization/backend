package com.example.pulsepost.domain.dtos.post;

import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.domain.enums.TypePostEnum;

public record PostUpdateDto(String title, String description, TypePostEnum typePost, MultipartFile file) {
}
