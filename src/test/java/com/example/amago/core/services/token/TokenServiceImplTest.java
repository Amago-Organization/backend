package com.example.amago.core.services.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class TokenServiceImplTest {
    private TokenServiceImpl service;

    @BeforeEach
    void setup() {
        service = new TokenServiceImpl();
        ReflectionTestUtils.setField(service, "secret", "test-secret");
    }

    @Test
    @DisplayName("Deve gerar token com sucesso")
    void shouldGenerateTokenSuccessfully() {

        String token = service.generateToken("email@email.com", "123");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve validar token com sucesso")
    void shouldValidateTokenSuccessfully() {

        String email = "email@email.com";

        String token = service.generateToken(email, "123");

        String result = service.validateToken(token);

        assertEquals(email, result);
    }

    @Test
    @DisplayName("Deve falhar ao validar token inválido")
    void shouldFailWhenTokenIsInvalid() {

        assertThrows(Exception.class,
                () -> service.validateToken("token_invalido"));
    }
}
