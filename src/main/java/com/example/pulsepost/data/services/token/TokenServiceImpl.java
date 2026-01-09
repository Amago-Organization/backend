package com.example.pulsepost.data.services.token;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.pulsepost.domain.exceptions.DomainException;
import com.example.pulsepost.presentation.messages.ExceptionMessage;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("{api.security.token.secret}")
    private String secret;

    @Override
    public String generateToken(String email, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(email)
                    .withExpiresAt(LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
            return token;
        } catch (IllegalArgumentException e) {
            throw new DomainException(ExceptionMessage.token(e.toString()));
        } catch (JWTCreationException e) {
            throw new DomainException(ExceptionMessage.token(e.toString()));
        }
    }

    @Override
    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String tokenValidated = JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
        return tokenValidated;
    }

}
