package com.example.pulsepost.data.services.user;

import com.example.pulsepost.domain.dtos.Token.TokenDto;
import com.example.pulsepost.domain.dtos.User.UserDetailDto;
import com.example.pulsepost.domain.models.UserModel;

public interface UserService {
    public UserDetailDto register(UserModel data);

    public TokenDto login(UserModel data);
}
