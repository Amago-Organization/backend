package com.example.amago.features.post.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.services.upload.CloudinaryUploadService;
import com.example.amago.core.utils.messages.ExceptionMessage;
import com.example.amago.features.post.dto.request.PostRegisterDto;
import com.example.amago.features.post.dto.request.PostUpdateDto;
import com.example.amago.features.post.dto.response.PostDetailDto;
import com.example.amago.features.post.dto.response.PostListDto;
import com.example.amago.features.post.enums.PostTypeEnum;
import com.example.amago.features.post.model.PostModel;
import com.example.amago.features.post.repository.PostRepository;
import com.example.amago.features.user.mapper.PostMapper;
import com.example.amago.features.user.model.UserModel;

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

        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (!(principal instanceof UserModel user)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

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
        }

        post = postRepository.save(post);

        if (file != null && !file.isEmpty()) {
            String fileUrl = cloudinaryUploadService.uploadFile(file, post.getId());
            post.setFile(fileUrl);
            post = postRepository.save(post);
        }

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

        PostModel post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post")));

        post.setUpdatedAt(LocalDateTime.now());

        MultipartFile file = data.file();

        if (file != null && !file.isEmpty()) {

            boolean oldIsVideo = post.getPostType() == PostTypeEnum.VIDEO;

            if (post.getFile() != null && !post.getFile().isEmpty()) {
                cloudinaryUploadService.deleteFile(post.getId(), oldIsVideo);
            }

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

        if (data.title() != null) {
            post.setTitle(data.title());
        }

        if (data.description() != null) {
            post.setDescription(data.description());
        }

        post = postRepository.save(post);

        return PostMapper.toDetailDto(post);

    }

    @Override
    @Transactional
    public void delete(String id) {

        PostModel post = postRepository.findById(id)
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Post")));

        if (post.getFile() != null && !post.getFile().isEmpty()) {

            boolean isVideo = post.getPostType() == PostTypeEnum.VIDEO;

            cloudinaryUploadService.deleteFile(post.getId(), isVideo);
        }

        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostListDto list() {
        return PostMapper.toListDto(postRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    @Transactional
    public PostListDto listByPostType(String type) {
        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (!(principal instanceof UserModel user)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        return PostMapper.toListDto(
                postRepository.findAllByPostTypeAndUserIdOrderByCreatedAtDesc(PostTypeEnum.valueOf(type), user));
    }

}
