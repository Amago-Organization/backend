package com.example.pulsepost.presentation.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsepost.data.services.post.PostService;
import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.presentation.validations.GroupValidation;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private PostService postService;

    @PostMapping("/register")
    public PostDetailDto registerUser(@Validated(GroupValidation.Create.class) PostRegisterDto data) {
        return postService.register(data);
    }
}
