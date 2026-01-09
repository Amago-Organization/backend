package com.example.pulsepost.presentation.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsepost.data.services.user.UserService;
import com.example.pulsepost.domain.dtos.Token.TokenDto;
import com.example.pulsepost.domain.dtos.User.UserDetailDto;
import com.example.pulsepost.domain.models.UserModel;
import com.example.pulsepost.presentation.validations.GroupValidation;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class Controller {
    private UserService userService;

    @PostMapping("/register")
    public UserDetailDto registerUser(@Validated(GroupValidation.Create.class) @RequestBody UserModel data) {
        return userService.register(data);
    }

    @PostMapping("/login")
    public TokenDto LoginUser(@Validated(GroupValidation.Login.class) @RequestBody UserModel data) {
        return userService.login(data);
    }

}
