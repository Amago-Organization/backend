package com.example.amago.features.user.dto.response;

import java.time.LocalDateTime;

public record UserDetailDto(String id, String name, String email, String bio, String image, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
