package com.example.amago.features.post.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.amago.core.utils.validations.GroupValidation;
import com.example.amago.features.post.dto.request.PostRegisterDto;
import com.example.amago.features.post.dto.request.PostUpdateDto;
import com.example.amago.features.post.dto.response.PostDetailDto;
import com.example.amago.features.post.dto.response.PostListDto;
import com.example.amago.features.post.service.PostService;

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

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        postService.delete(id);
    }

    @GetMapping("/list")
    public PostListDto list() {
        return postService.list();
    }

    @GetMapping("/list/post-type/{type}")
    public PostListDto listByPostType(@PathVariable String type) {
        return postService.listByPostType(type);
    }
}
