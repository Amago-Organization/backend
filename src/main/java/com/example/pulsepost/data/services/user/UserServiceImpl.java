package com.example.pulsepost.data.services.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pulsepost.data.repositories.UserRepository;
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

}
