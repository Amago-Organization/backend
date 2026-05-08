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
import com.example.amago.features.user.dto.request.UserLoginDto;
import com.example.amago.features.user.dto.request.UserRegisterDto;
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

        user.setPassword(encoder.encode("Senha@123"));
    }

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() {

        UserRegisterDto registerDto = new UserRegisterDto("Lázaro", "lazaro@gmail.com", "Senha@123");

        when(userRepository.findByEmail(registerDto.email()))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(UserModel.class)))
                .thenReturn(user);

        UserDetailDto result = service.register(registerDto);

        assertNotNull(result);
        assertEquals("Lázaro", result.name());
        assertEquals("lazaro@gmail.com", result.email());
        assertNull(result.bio(), "bio deve ser nulo no cadastro");
        assertNull(result.image(), "image deve ser nulo no cadastro");
        assertNull(result.updatedAt(), "updated_at deve ser nulo no cadastro");
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Deve falhar ao registrar com email já existente")
    void shouldFailRegisterWhenEmailExists() {

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        DomainException exception = assertThrows(DomainException.class,
                () -> service.register(new UserRegisterDto(user.getName(), user.getEmail(), "Senha@123")));

        assertEquals("E-mail já em uso!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void shouldLoginSuccessfully() {

        UserLoginDto loginUser = new UserLoginDto("lazaro@gmail.com", "Senha@123");

        when(userRepository.findByEmail(loginUser.email()))
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

        UserLoginDto loginUser = new UserLoginDto("teste@gmail.com", "Senha@123");

        when(userRepository.findByEmail(loginUser.email()))
                .thenReturn(Optional.empty());

        assertThrows(DomainException.class,
                () -> service.login(loginUser));
    }

    @Test
    @DisplayName("Deve falhar login com senha inválida")
    void shouldFailLoginWhenPasswordInvalid() {

        UserLoginDto loginUser = new UserLoginDto("lazaro@gmail.com", "senhaErrad4");

        when(userRepository.findByEmail(loginUser.email()))
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
