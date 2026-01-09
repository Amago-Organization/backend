package com.example.pulsepost.presentation.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import com.example.pulsepost.data.repositories.UserRepository;
import com.example.pulsepost.data.services.token.TokenService;
import com.example.pulsepost.domain.models.UserModel;

@Component
@AllArgsConstructor
public class SecurityFilterConfig extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private String recoveryToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recoveryToken(request);

        if (token != null) {
            String email = tokenService.validateToken(token);

            Optional<UserModel> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.get(),
                                null,
                                List.of()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
