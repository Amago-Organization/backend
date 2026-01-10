package com.example.pulsepost.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.pulsepost.domain.enums.PostTypeEnum;
import com.example.pulsepost.domain.models.PostModel;
import com.example.pulsepost.domain.models.UserModel;

@CrossOrigin(origins = "*")
public interface PostRepository extends JpaRepository<PostModel, String> {
    List<PostModel> findAllByOrderByCreatedAtDesc();

    List<PostModel> findAllByPostTypeAndUserIdOrderByCreatedAtDesc(PostTypeEnum postType, UserModel userId);
}
