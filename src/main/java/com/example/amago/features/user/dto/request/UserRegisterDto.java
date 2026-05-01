package com.example.amago.features.user.dto.request;


import com.example.amago.core.utils.validations.GroupValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(@NotBlank(groups = GroupValidation.Create.class) @Size(max = 255) String name,
        @NotBlank(groups = {
                GroupValidation.Create.class, GroupValidation.Login.class }) @Size(max = 255) @Email String email,
        @NotBlank(groups = { GroupValidation.Create.class,
                GroupValidation.Login.class }) @Size(min = 6) String passaword) {

}
