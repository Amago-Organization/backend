package com.example.pulsepost.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pulsepost.domain.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
}
