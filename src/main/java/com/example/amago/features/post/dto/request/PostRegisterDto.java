package com.example.amago.features.post.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.amago.core.utils.validations.GroupValidation;
import com.example.amago.features.user.model.UserModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRegisterDto(
                UserModel userId,
                @NotNull(groups = GroupValidation.Create.class) @Size(max = 255) String title,
                @NotNull(groups = GroupValidation.Create.class) String description,
                MultipartFile file) {

}
