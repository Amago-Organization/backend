package com.example.pulsepost.domain.dtos.post;

import java.time.LocalDateTime;

import com.example.pulsepost.domain.enums.PostTypeEnum;

public record PostDetailDto(String id, String title, String description, PostTypeEnum postType, String file, LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
