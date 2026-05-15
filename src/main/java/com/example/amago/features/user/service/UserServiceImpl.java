package com.example.amago.features.user.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.services.token.TokenService;
import com.example.amago.core.services.upload.CloudinaryUploadService;
import com.example.amago.core.utils.messages.ExceptionMessage;
import com.example.amago.features.post.mapper.UserMapper;
import com.example.amago.features.user.dto.request.UserLoginDto;
import com.example.amago.features.user.dto.request.UserRegisterDto;
import com.example.amago.features.user.dto.request.UserUpdateDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.dto.response.UserTokenDto;
import com.example.amago.features.user.model.UserModel;
import com.example.amago.features.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenService tokenService;
    private CloudinaryUploadService cloudinaryUploadService;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public UserDetailDto register(UserRegisterDto data) {
        boolean emailUsed = userRepository.findByEmail(data.email()).isPresent();
        if (emailUsed) {
            throw new DomainException(ExceptionMessage.attributeUsed("E-mail"));
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserModel user = new UserModel();
        user.setName(data.name());
        user.setEmail(data.email());
        user.setPassword(encryptedPassword);

        return UserMapper.toDetailDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserTokenDto login(UserLoginDto data) {

        UserModel user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new DomainException(ExceptionMessage.invalidAuthentication));

        boolean passwordValid = new BCryptPasswordEncoder()
                .matches(data.password(), user.getPassword());

        if (!passwordValid) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        String token = tokenService.generateToken(user.getEmail(), user.getPassword());

        return new UserTokenDto(token);
    }

    @Override
    @Transactional
    public UserDetailDto detail() {

        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (!(principal instanceof UserModel user)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        return UserMapper.toDetailDto(user);
    }

    @Override
    @Transactional
    public UserDetailDto update(UserUpdateDto data) {

        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (!(principal instanceof UserModel user)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        if (data.name() != null) {
            user.setName(data.name());
        }

        if (data.bio() != null) {
            user.setBio(data.bio());
        }

        user.setUpdatedAt(LocalDateTime.now());

        UserModel updatedUser = userRepository.save(user);

        MultipartFile file = data.image();

        if (file != null && !file.isEmpty()) {

            if (updatedUser.getImage() != null && !updatedUser.getImage().isEmpty()) {
                cloudinaryUploadService.deleteFile(updatedUser.getId(), false);
            }

            String imageUrl = cloudinaryUploadService.uploadFile(file, updatedUser.getId());
            updatedUser.setImage(imageUrl);

            updatedUser = userRepository.save(updatedUser);
        }

        return UserMapper.toDetailDto(updatedUser);
    }

    @Override
    @Transactional
    public void logout() {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new DomainException(
                    ExceptionMessage.invalidAuthentication);
        }

        String token = authHeader.replace("Bearer ", "");

        tokenService.revokeToken(token);

        SecurityContextHolder.clearContext();
    }

}
