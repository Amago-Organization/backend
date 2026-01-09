package com.example.pulsepost.domain.dtos.post;

import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.domain.enums.TypePostEnum;
import com.example.pulsepost.domain.models.UserModel;
import com.example.pulsepost.presentation.validations.GroupValidation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRegisterDto(
        @NotNull(groups = GroupValidation.Create.class) UserModel userId,
        @NotNull(groups = GroupValidation.Create.class) @Size(max = 255) String title,
        @NotNull(groups = GroupValidation.Create.class) String description,
        TypePostEnum typePost, 
        MultipartFile file) {

}
