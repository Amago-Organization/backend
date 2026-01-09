package com.example.pulsepost.data.services.post;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.data.repositories.PostRepository;
import com.example.pulsepost.data.services.upload.CloudinaryUploadService;
import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.enums.TypePostEnum;
import com.example.pulsepost.domain.exceptions.DomainException;
import com.example.pulsepost.domain.mappers.post.PostMapper;
import com.example.pulsepost.domain.models.PostModel;
import com.example.pulsepost.presentation.messages.ExceptionMessage;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private PostMapper postMapper;
    private CloudinaryUploadService cloudinaryUploadService;

    @Override
    @Transactional
    public PostDetailDto register(PostRegisterDto data) {

        PostModel post = postRepository.save(postMapper.toEntity(data));

        MultipartFile file = data.file();
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();

            if (contentType != null && contentType.startsWith("video/")) {
                post.setTypePost(TypePostEnum.VIDEO);
            } else if (contentType != null && contentType.startsWith("image/")) {
                post.setTypePost(TypePostEnum.IMAGE);
            } else {
                post.setTypePost(TypePostEnum.TEXT);
            }

            String fileUrl = cloudinaryUploadService.uploadFile(file, post.getId());
            post.setFile(fileUrl);

            post = postRepository.save(post);
        }

        return PostMapper.toDetailDto(post);
    }

    @Override
    public PostDetailDto detail(String id) {
        return PostMapper.toDetailDto(postRepository.findById(id).map(p -> p)
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post"))));
    }

}
