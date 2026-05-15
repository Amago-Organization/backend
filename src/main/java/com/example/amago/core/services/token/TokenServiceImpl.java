package com.example.amago.core.services.token;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.amago.core.exceptions.DomainException;
import com.example.amago.core.utils.messages.ExceptionMessage;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("{api.security.token.secret}")
    private String secret;

    private final Set<String> revokedTokens = new HashSet<>();

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
        if (isRevoked(token)) {
            throw new DomainException("Token inválido.");
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);
        String tokenValidated = JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
        return tokenValidated;
    }

    @Override
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    @Override
    public boolean isRevoked(String token) {
        return revokedTokens.contains(token);
    }

}
