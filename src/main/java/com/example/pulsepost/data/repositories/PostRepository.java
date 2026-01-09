package com.example.pulsepost.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.pulsepost.domain.models.PostModel;

@CrossOrigin(origins = "*")
public interface PostRepository extends JpaRepository<PostModel, String> {
}
