package com.example.pulsepost.data.services.post;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.data.repositories.PostRepository;
import com.example.pulsepost.data.services.upload.CloudinaryUploadService;
import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostListDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.dtos.post.PostUpdateDto;
import com.example.pulsepost.domain.enums.PostTypeEnum;
import com.example.pulsepost.domain.exceptions.DomainException;
import com.example.pulsepost.domain.mappers.post.PostMapper;
import com.example.pulsepost.domain.models.PostModel;
import com.example.pulsepost.domain.models.UserModel;
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

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserModel)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        UserModel user = (UserModel) principal;

        PostModel post = postMapper.toEntityRegister(data);

        post.setUserId(user);

        MultipartFile file = data.file();
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();

            if (contentType != null && contentType.startsWith("video/")) {
                post.setPostType(PostTypeEnum.VIDEO);
            } else if (contentType != null && contentType.startsWith("image/")) {
                post.setPostType(PostTypeEnum.IMAGE);
            } else {
                post.setPostType(PostTypeEnum.TEXT);
            }

            String fileUrl = cloudinaryUploadService.uploadFile(file, post.getId());
            post.setFile(fileUrl);

        }
        post = postRepository.save(post);
        return PostMapper.toDetailDto(post);
    }

    @Override
    @Transactional
    public PostDetailDto detail(String id) {
        return PostMapper.toDetailDto(postRepository.findById(id).map(p -> p)
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post"))));
    }

    @Override
    @Transactional
    public PostDetailDto update(String id, PostUpdateDto data) {

        PostModel post = postRepository.findById(id).map(p -> p)
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post")));

        if (data != null) {
            post.setUpdatedAt(LocalDateTime.now());
        }

        String fileUrl = null;
        MultipartFile file = data.file();
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();

            boolean isVideo = false;

            if (contentType != null && contentType.startsWith("video/")) {
                post.setPostType(PostTypeEnum.VIDEO);
                isVideo = true;
            } else if (contentType != null && contentType.startsWith("image/")) {
                post.setPostType(PostTypeEnum.IMAGE);
                isVideo = false;
            } else {
                post.setPostType(PostTypeEnum.TEXT);
                isVideo = false;
            }

            if (post.getFile() != null && !post.getFile().isEmpty()) {
                cloudinaryUploadService.deleteFile(post.getId(), isVideo);
            }

            fileUrl = cloudinaryUploadService.uploadFile(file, post.getId());
            post.setFile(fileUrl);
        }
        if (data.title() != null) {
            post.setTitle(data.title());
        }
        if (data.description() != null) {

            post.setDescription(data.description());
        }

        return PostMapper.toDetailDto(postRepository.save(post));

    }

    @Override
    @Transactional
    public void delete(String id) {
        postRepository.deleteById(postRepository.findById(id).map(p -> p.getId())
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post"))));
    }

    @Override
    @Transactional
    public PostListDto list() {
        return PostMapper.toListDto(postRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    @Transactional
    public PostListDto listByPostType(String type) {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserModel)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        UserModel user = (UserModel) principal;

        return PostMapper.toListDto(postRepository.findAllByPostTypeAndUserIdOrderByCreatedAtDesc(PostTypeEnum.valueOf(type), user));
    }

}
