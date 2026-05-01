package com.example.amago.features.user.service;

import com.example.amago.features.user.dto.request.UserUpdateDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.dto.response.UserTokenDto;
import com.example.amago.features.user.model.UserModel;

public interface UserService {
    public UserDetailDto register(UserModel data);

    public UserTokenDto login(UserModel data);

    public UserDetailDto detail();

    public UserDetailDto update(UserUpdateDto data);

}
