package com.example.amago.features.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.services.token.TokenService;
import com.example.amago.core.services.upload.CloudinaryUploadService;
import com.example.amago.features.user.dto.request.UserUpdateDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.dto.response.UserTokenDto;
import com.example.amago.features.user.model.UserModel;
import com.example.amago.features.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private CloudinaryUploadService cloudinaryUploadService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserServiceImpl service;

    private UserModel user;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();

        user = new UserModel();
        user.setId("1");
        user.setName("Lázaro");
        user.setEmail("lazaro@gmail.com");

        user.setPassword(encoder.encode("123456"));
    }

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() {

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(UserModel.class)))
                .thenReturn(user);

        UserDetailDto result = service.register(user);

        assertNotNull(result);
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Deve falhar ao registrar com email já existente")
    void shouldFailRegisterWhenEmailExists() {

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(DomainException.class,
                () -> service.register(user));
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void shouldLoginSuccessfully() {

        UserModel loginUser = new UserModel();
        loginUser.setEmail("lazaro@gmail.com");
        loginUser.setPassword("123456"); 

        when(userRepository.findByEmail(loginUser.getEmail()))
                .thenReturn(Optional.of(user));

        when(tokenService.generateToken(any(), any()))
                .thenReturn("token123");

        UserTokenDto result = service.login(loginUser);

        assertNotNull(result);
        assertEquals("token123", result.token());
    }

    @Test
    @DisplayName("Deve falhar login quando email não existe")
    void shouldFailLoginWhenEmailNotFound() {

        UserModel loginUser = new UserModel();
        loginUser.setEmail("teste@gmail.com");
        loginUser.setPassword("123456");

        when(userRepository.findByEmail(loginUser.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(DomainException.class,
                () -> service.login(loginUser));
    }

    @Test
    @DisplayName("Deve falhar login com senha inválida")
    void shouldFailLoginWhenPasswordInvalid() {

        UserModel loginUser = new UserModel();
        loginUser.setEmail("lazaro@gmail.com");
        loginUser.setPassword("senhaErrada");

        when(userRepository.findByEmail(loginUser.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(DomainException.class,
                () -> service.login(loginUser));
    }

    @Test
    @DisplayName("Deve retornar detalhe com sucesso")
    void shouldReturnDetailSuccessfully() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        UserDetailDto result = service.detail();

        assertNotNull(result);
        assertEquals("Lázaro", result.name());
    }

    @Test
    @DisplayName("Deve falhar detalhe sem autenticação")
    void shouldFailDetailWithoutAuth() {

        SecurityContextHolder.clearContext();

        assertThrows(DomainException.class,
                () -> service.detail());
    }

    @Test
    @DisplayName("Deve atualizar nome e bio com sucesso")
    void shouldUpdateNameAndBioSuccessfully() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        UserUpdateDto dto = new UserUpdateDto("Novo Nome", "Nova Bio", null);

        when(userRepository.save(any(UserModel.class)))
                .thenReturn(user);

        UserDetailDto result = service.update(dto);

        assertNotNull(result);
        verify(userRepository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar imagem com sucesso")
    void shouldUpdateImageSuccessfully() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        when(multipartFile.isEmpty()).thenReturn(false);

        when(cloudinaryUploadService.uploadFile(any(), any()))
                .thenReturn("url-img");

        when(userRepository.save(any(UserModel.class)))
                .thenReturn(user);

        UserUpdateDto dto = new UserUpdateDto("Novo Nome", "Bio", multipartFile);

        UserDetailDto result = service.update(dto);

        assertNotNull(result);
        verify(cloudinaryUploadService).uploadFile(any(), any());
    }

    @Test
    @DisplayName("Deve falhar update sem autenticação")
    void shouldFailUpdateWithoutAuthentication() {

        SecurityContextHolder.clearContext();

        UserUpdateDto dto = new UserUpdateDto("Nome", "Bio", null);

        assertThrows(DomainException.class,
                () -> service.update(dto));
    }

}
