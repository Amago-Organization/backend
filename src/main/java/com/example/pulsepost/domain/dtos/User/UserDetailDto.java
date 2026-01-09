package com.example.pulsepost.domain.dtos.User;

import java.time.LocalDateTime;

public record UserDetailDto(String id, String name, String email, String bio, String image, LocalDateTime created,
        LocalDateTime updated) {
}
