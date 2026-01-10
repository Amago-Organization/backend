package com.example.pulsepost.domain.mappers.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostListDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.dtos.post.PostUpdateDto;
import com.example.pulsepost.domain.dtos.user.summary.UserSummaryDto;
import com.example.pulsepost.domain.enums.PostTypeEnum;
import com.example.pulsepost.domain.models.PostModel;

@Component
public class PostMapper {

    public PostRegisterDto toRegisterDto(PostModel model, MultipartFile file) {
        if (model == null) {
            return null;
        }
        return new PostRegisterDto(model.getUserId(), model.getTitle(), model.getDescription(),
                file);
    }

    public PostUpdateDto toUpdateDto(PostModel model, MultipartFile file) {
        if (model == null) {
            return null;
        }
        return new PostUpdateDto(model.getTitle(), model.getDescription(), model.getPostType(), file);
    }

    public PostModel toEntityRegister(PostRegisterDto dto) {
        if (dto == null) {
            return null;
        }

        PostModel model = new PostModel();

        model.setUserId(dto.userId());
        model.setTitle(dto.title());
        model.setDescription(dto.description());
        model.setPostType(PostTypeEnum.TEXT);
        model.setCreatedAt(LocalDateTime.now());
        return model;
    }

    public PostModel toEntityUpdate(PostUpdateDto dto, String fileUrl) {
        if (dto == null) {
            return null;
        }

        PostModel model = new PostModel();

        if (dto.title() != null) {
            model.setTitle(dto.title());
        }
        if (dto.description() != null) {
            model.setDescription(dto.description());

        }
        if (dto.file() != null) {
            model.setFile(fileUrl);
            model.setPostType(dto.postType());
        }

        model.setUpdatedAt(LocalDateTime.now());
        return model;
    }

    public static PostDetailDto toDetailDto(PostModel model) {
        UserSummaryDto user = null;

        if (model.getUserId() != null) {
            user = new UserSummaryDto(
                    model.getUserId().getId(),
                    model.getUserId().getName(),
                    model.getUserId().getImage());
        }
        return new PostDetailDto(model.getId(), model.getTitle(), model.getDescription(), model.getPostType(),
                model.getFile(), model.getCreatedAt(), model.getUpdatedAt(), user);
    }

    public static PostListDto toListDto(List<PostModel> modelList) {
        List<PostDetailDto> posts = modelList.stream()
                .map(PostMapper::toDetailDto)
                .collect(Collectors.toList());

        return new PostListDto(posts);
    }
}
