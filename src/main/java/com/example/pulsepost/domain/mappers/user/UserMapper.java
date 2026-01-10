package com.example.pulsepost.domain.mappers.user;

import org.springframework.stereotype.Component;

import com.example.pulsepost.domain.dtos.user.UserDetailDto;
import com.example.pulsepost.domain.dtos.user.UserRegisterDto;
import com.example.pulsepost.domain.models.UserModel;

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
