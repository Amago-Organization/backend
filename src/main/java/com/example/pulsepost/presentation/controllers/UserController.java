package com.example.pulsepost.presentation.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsepost.data.services.user.UserService;
import com.example.pulsepost.domain.dtos.token.TokenDto;
import com.example.pulsepost.domain.dtos.user.UserDetailDto;
import com.example.pulsepost.domain.dtos.user.UserUpdateDto;
import com.example.pulsepost.domain.models.UserModel;
import com.example.pulsepost.presentation.validations.GroupValidation;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public UserDetailDto registerUser(@Validated(GroupValidation.Create.class) @RequestBody UserModel data) {
        return userService.register(data);
    }

    @PostMapping("/login")
    public TokenDto LoginUser(@Validated(GroupValidation.Login.class) @RequestBody UserModel data) {
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
