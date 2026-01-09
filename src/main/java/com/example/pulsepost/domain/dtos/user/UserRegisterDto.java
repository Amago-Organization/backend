package com.example.pulsepost.domain.dtos.user;

import com.example.pulsepost.presentation.validations.GroupValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(@NotBlank(groups = GroupValidation.Create.class) @Size(max = 255) String name,
        @NotBlank(groups = {
                GroupValidation.Create.class, GroupValidation.Login.class }) @Size(max = 255) @Email String email,
        @NotBlank(groups = { GroupValidation.Create.class,
                GroupValidation.Login.class }) @Size(min = 6) String passaword) {

}
