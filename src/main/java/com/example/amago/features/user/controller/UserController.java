package com.example.amago.features.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amago.core.utils.validations.GroupValidation;
import com.example.amago.features.user.dto.request.UserLoginDto;
import com.example.amago.features.user.dto.request.UserRegisterDto;
import com.example.amago.features.user.dto.request.UserUpdateDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.dto.response.UserTokenDto;
import com.example.amago.features.user.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public UserDetailDto registerUser(@Validated(GroupValidation.Create.class) @RequestBody UserRegisterDto data) {
        return userService.register(data);
    }

    @PostMapping("/login")
    public UserTokenDto LoginUser(@Validated(GroupValidation.Login.class) @RequestBody UserLoginDto data) {
        return userService.login(data);
    }

    @GetMapping("/detail")
    public UserDetailDto DetailUser() {
        return userService.detail();
    }

    @PatchMapping("/update")
    public UserDetailDto UpdateUser(UserUpdateDto data) {
        return userService.update(data);
    }

}
