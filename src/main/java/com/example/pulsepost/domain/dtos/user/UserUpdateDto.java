package com.example.pulsepost.domain.dtos.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Size;

public record UserUpdateDto(@Size(max = 255) String name, String bio, MultipartFile image) {

}
