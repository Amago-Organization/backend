package com.example.pulsepost.domain.mappers.post;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.pulsepost.domain.dtos.post.PostDetailDto;
import com.example.pulsepost.domain.dtos.post.PostRegisterDto;
import com.example.pulsepost.domain.enums.TypePostEnum;
import com.example.pulsepost.domain.models.PostModel;

@Component
public class PostMapper {

    public PostRegisterDto toRegisterDto(PostModel model, MultipartFile file) {
        if (model == null) {
            return null;
        }
        return new PostRegisterDto(model.getUseId(), model.getTitle(), model.getDescription(), model.getTypePost(),
                file);
    }

    public PostModel toEntity(PostRegisterDto dto) {
        if (dto == null) {
            return null;
        }

        PostModel model = new PostModel();

        model.setUseId(dto.userId());
        model.setTitle(dto.title());
        model.setDescription(dto.description());
        if (dto.typePost() == null) {
            model.setTypePost(TypePostEnum.TEXT);
        } else {
            model.setTypePost(dto.typePost());

        }
        model.setUpdatedAt(LocalDateTime.now());
        return model;
    }

    public static PostDetailDto toDetailDto(PostModel model) {
        return new PostDetailDto(model.getId(), model.getTitle(), model.getDescription(), model.getTypePost(),
                model.getFile(), model.getCreatedAt(), model.getUpdatedAt());
    }
}
