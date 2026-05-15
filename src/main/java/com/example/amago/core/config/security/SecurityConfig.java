package com.example.amago.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private SecurityFilterConfig securityFilterConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/detail").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/user/update").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/logout").authenticated()


                        .requestMatchers(HttpMethod.POST, "/post/register").authenticated()
                        .requestMatchers(HttpMethod.GET, "/post/detail/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/post/update/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/post/delete/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/post/list").authenticated()
                        .requestMatchers(HttpMethod.GET, "/post/list/post-type/**").authenticated()

                )

                .addFilterBefore(securityFilterConfig, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}