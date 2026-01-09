package com.example.pulsepost.data.services.user;

import com.example.pulsepost.domain.dtos.token.TokenDto;
import com.example.pulsepost.domain.dtos.user.UserDetailDto;
import com.example.pulsepost.domain.dtos.user.UserUpdateDto;
import com.example.pulsepost.domain.models.UserModel;

public interface UserService {
    public UserDetailDto register(UserModel data);

    public TokenDto login(UserModel data);

    public UserDetailDto detail();

    public UserDetailDto update(UserUpdateDto data);

}
