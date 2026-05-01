package com.example.amago.features.post.service;

import com.example.amago.features.post.dto.request.PostRegisterDto;
import com.example.amago.features.post.dto.request.PostUpdateDto;
import com.example.amago.features.post.dto.response.PostDetailDto;
import com.example.amago.features.post.dto.response.PostListDto;

public interface PostService {
    public PostDetailDto register(PostRegisterDto data);

    public PostDetailDto detail(String id);

    public PostDetailDto update(String id, PostUpdateDto data);

    public void delete(String id);

    public PostListDto list();

    public PostListDto listByPostType(String type);

}
