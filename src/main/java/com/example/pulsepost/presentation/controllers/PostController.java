package com.example.pulsepost.presentation.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pulsepost.data.services.post.PostService;
import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.dtos.post.PostUpdateDto;
import com.example.pulsepost.presentation.validations.GroupValidation;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private PostService postService;

    @PostMapping("/register")
    public PostDetailDto register(@Validated(GroupValidation.Create.class) PostRegisterDto data) {
        return postService.register(data);
    }


    @GetMapping("/detail/{id}")
    public PostDetailDto detail(@PathVariable String id) {
        return postService.detail(id);
    }

    @PatchMapping("/update/{id}")
    public PostDetailDto update(@PathVariable String id, PostUpdateDto data) {
        return postService.update(id, data);
    }
}
