package com.example.pulsepost.data.services.token;

public interface TokenService {
    public String generateToken(String email, String password);

    public String validateToken(String token);

}
