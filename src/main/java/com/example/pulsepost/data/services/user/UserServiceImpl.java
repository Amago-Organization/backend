package com.example.pulsepost.data.services.user;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pulsepost.data.repositories.UserRepository;
import com.example.pulsepost.data.services.token.TokenService;
import com.example.pulsepost.domain.dtos.Token.TokenDto;
import com.example.pulsepost.domain.dtos.User.UserDetailDto;
import com.example.pulsepost.domain.exceptions.DomainException;
import com.example.pulsepost.domain.mappers.User.UserMapper;
import com.example.pulsepost.domain.models.UserModel;
import com.example.pulsepost.presentation.messages.ExceptionMessage;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenService tokenService;

    @Override
    @Transactional
    public UserDetailDto register(UserModel data) {
        boolean emailUsed = userRepository.findByEmail(data.getEmail()).isPresent();
        if (emailUsed) {
            throw new DomainException(ExceptionMessage.attributeUsed("E-mail"));
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

        data.setPassword(encryptedPassword);

        return UserMapper.toDetailDto(userRepository.save(data));

    }

    @Override
    @Transactional
    public TokenDto login(UserModel data) {
        Optional<UserModel> user = userRepository.findByEmail(data.getEmail())
                .map(u -> u);
        boolean passwordValid = new BCryptPasswordEncoder().matches(data.getPassword(), user.get().getPassword());
        if (user == null || !passwordValid) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        String token = tokenService.generateToken(user.get().getEmail(), user.get().getPassword());
        return new TokenDto(token);
    }

    @Override
    @Transactional
    public UserDetailDto detail() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserModel)) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }

        UserModel user = (UserModel) principal;

        return UserMapper.toDetailDto(user);
    }

}
