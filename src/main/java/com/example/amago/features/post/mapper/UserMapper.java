package com.example.amago.features.post.mapper;

import org.springframework.stereotype.Component;

import com.example.amago.features.user.dto.request.UserRegisterDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.model.UserModel;

@Component
public class UserMapper {

    public UserRegisterDto toRegisterDto(UserModel user) {
        if (user == null) {
            return null;
        }
        return new UserRegisterDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserDetailDto toDetailDto(UserModel model) {
        return new UserDetailDto(
                model.getId(),
                model.getName(),
                model.getEmail(),
                model.getBio(),
                model.getImage(),
                model.getCreatedAt(),
                model.getUpdatedAt());
    }
}
