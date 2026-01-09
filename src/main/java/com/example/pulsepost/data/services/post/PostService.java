package com.example.pulsepost.data.services.post;

import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostListDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.dtos.post.PostUpdateDto;

public interface PostService {
    public PostDetailDto register(PostRegisterDto data);

    public PostDetailDto detail(String id);

    public PostDetailDto update(String id, PostUpdateDto data);

    public void delete(String id);

    public PostListDto list();

}
