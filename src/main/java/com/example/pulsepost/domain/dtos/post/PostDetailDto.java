package com.example.pulsepost.domain.dtos.post;

import java.time.LocalDateTime;

import com.example.pulsepost.domain.enums.TypePostEnum;

public record PostDetailDto(String id, String title, String description, TypePostEnum typePost, String file, LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
