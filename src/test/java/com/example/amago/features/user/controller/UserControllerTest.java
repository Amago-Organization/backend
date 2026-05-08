package com.example.amago.features.user.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.exceptions.ExceptionHandle;
import com.example.amago.features.user.dto.request.UserLoginDto;
import com.example.amago.features.user.dto.request.UserRegisterDto;
import com.example.amago.features.user.dto.response.UserDetailDto;
import com.example.amago.features.user.dto.response.UserTokenDto;
import com.example.amago.features.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDetailDto userDetailDto;
    private UserTokenDto userTokenDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ExceptionHandle())
                .build();
        objectMapper = new ObjectMapper();

        userDetailDto = new UserDetailDto(
                "1",
                "Lázaro",
                "lazaro@gmail.com",
                null,
                null,
                LocalDateTime.now(),
                null
        );

        userTokenDto = new UserTokenDto("jwt-token-here");
    }

    @Test
    @DisplayName("Registrar usuário com sucesso")
    void shouldRegisterUserSuccessfully() throws Exception {
        UserRegisterDto requestDto = new UserRegisterDto("Lázaro", "lazaro@gmail.com", "Senha@123");

        when(userService.register(any(UserRegisterDto.class)))
                .thenReturn(userDetailDto);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Lázaro"))
                .andExpect(jsonPath("$.email").value("lazaro@gmail.com"))
                .andExpect(jsonPath("$.bio").isEmpty())
                .andExpect(jsonPath("$.image").isEmpty())
                .andExpect(jsonPath("$.updatedAt").isEmpty());
    }

    @Test
    @DisplayName("Falha ao registrar usuário quando email já existe")
    void shouldFailRegisterWhenEmailAlreadyExists() throws Exception {
        UserRegisterDto requestDto = new UserRegisterDto("Lázaro", "lazaro@gmail.com", "Senha@123");

        doThrow(new DomainException("E-mail já em uso!"))
                .when(userService).register(any(UserRegisterDto.class));

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Falha ao registrar usuário com formato de email inválido")
    void shouldFailRegisterWhenEmailFormatIsInvalid() throws Exception {
        UserRegisterDto requestDto = new UserRegisterDto("Lázaro", "email-invalido", "Senha@123");

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Falha ao registrar usuário quando senha não atende ao tamanho mínimo")
    void shouldFailRegisterWhenPasswordIsTooShort() throws Exception {
        UserRegisterDto requestDto = new UserRegisterDto("Lázaro", "lazaro@gmail.com", "S9p@");

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login de usuário com sucesso")
    void shouldLoginUserSuccessfully() throws Exception {
        UserLoginDto requestDto = new UserLoginDto("lazaro@gmail.com", "Senha@123");

        when(userService.login(any(UserLoginDto.class)))
                .thenReturn(userTokenDto);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-here"));
    }

    @Test
    @DisplayName("Falha ao fazer login com senha incorreta ou email não cadastrado")
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        UserLoginDto requestDto = new UserLoginDto("lazaro@gmail.com", "Senha@123");

        doThrow(new DomainException("E-mail ou senha inválidos"))
                .when(userService).login(any(UserLoginDto.class));

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("E-mail ou senha inválidos"));
    }

    @Test
    @DisplayName("Falha ao fazer login com senha que não atende aos critérios de complexidade")
    void shouldFailLoginWhenPasswordDoesNotMeetComplexity() throws Exception {
        UserLoginDto requestDto = new UserLoginDto("lazaro@gmail.com", "senha123");

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.fields.password").value("A senha deve conter ao menos 1 caractere maiúsculo, 1 caractere minúsculo, 1 número e 1 caractere especial!"));
    }

    @Test
    @DisplayName("Falha ao fazer login sem informar email")
    void shouldFailLoginWhenEmailIsMissing() throws Exception {
        String requestJson = "{\"password\":\"Senha@123\"}";

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.fields.email").exists());
    }

    @Test
    @DisplayName("Falha ao fazer login sem informar senha")
    void shouldFailLoginWhenPasswordIsMissing() throws Exception {
        String requestJson = "{\"email\":\"lazaro@gmail.com\"}";

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.fields.password").exists());
    }
}
