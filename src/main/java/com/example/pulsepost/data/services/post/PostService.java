package com.example.pulsepost.data.services.post;

import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;

public interface PostService {
    public PostDetailDto register(PostRegisterDto data);
    public PostDetailDto detail(String id);
}
