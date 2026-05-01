package com.example.amago.core.services.token;

public interface TokenService {
    public String generateToken(String email, String password);

    public String validateToken(String token);

}
