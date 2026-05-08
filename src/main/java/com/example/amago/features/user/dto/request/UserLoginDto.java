package com.example.amago.features.user.dto.request;

import com.example.amago.core.utils.messages.ExceptionMessage;
import com.example.amago.core.utils.regexs.RegexConstants;
import com.example.amago.core.utils.validations.GroupValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginDto(
        @NotBlank(groups = GroupValidation.Login.class) @Size(max = 255) @Email String email,
        @NotBlank(groups = GroupValidation.Login.class) @Size(min = 6, max = 10) @Pattern(regexp = RegexConstants.passwordAlfaNumericSpecialUpperLowerCase, message = ExceptionMessage.invalidPassword) String password) {
}
